package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.annotation.ExcelBeanMetaData;
import com.zhou.demo.excel.config.ApplicationContextAccessor;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.converter.EmptyConverter;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zhou.demo.excel.utils.BeanUtil.findSetMethod;
import static com.zhou.demo.excel.utils.SelfAnnotationUtil.getMemberValuesMap;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

@Log4j2
public class SimpleExcelFactory implements ExcelFactory {

    //用户自定义函数
//////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void validExcel(Workbook workBook, Excel excel) throws Exception {
    }

    @Override
    public boolean skipBlank() {
        return false;
    }

//////////////////////////////////////////////////////////////////////////////////////


    private final static Map<Class, ExcelBeanMetaData> cache = new ConcurrentHashMap<>();

    public ExcelPos findPos(String s, Row row) {
        short fNum = row.getFirstCellNum();
        short lNum = row.getLastCellNum();
        if (fNum < 0 || lNum < 0) return null;
        for (int i = fNum; i < lNum; i++) {
            Cell cell = row.getCell(i);
            //todo:添加表头校验
            String value = cell.getStringCellValue();
            if (s.equals(value)) return new ExcelPos(row.getRowNum(), i);
        }
        return null;
    }

    /**
     * 根据表头解析和bean的映射关系
     *
     * @param row   表头行
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Map<ColumnWrap, ExcelPos> resolveMapping(Row row, Class<T> clazz) {
        Map<ColumnWrap, ExcelPos> map = new HashMap();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            Column c = f.getAnnotation(Column.class);
            ExcelPos pos;
            if (c != null && (pos = findPos(c.headerName(), row)) != null) {
                //获取set方法的名称
                String setMethodName = findSetMethod(f, true);
                String initSetMethodName = c.setter();
                if (initSetMethodName.equals("") && setMethodName != null) {
                    //获取注解内部的值map
                    Map<String, Object> valuesMap = getMemberValuesMap(c);
                    //如果用户没有自定义setter,则使用默认的setter方法
                    valuesMap.put("setter", setMethodName);
                }
                //todo:添加全局缓存避免重复解析
                map.put(new ColumnWrap(c, f), pos);
            }
            //else skip
        }
//        cache.put(clazz, map);
        return map;
    }


    private final <T> Object convert(String rawValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws ExcelDataWrongException {
        Column column = cw.getColumn();
        ConversionService conversionService = ApplicationContextAccessor.getApplicationContext().getBean(ConversionService.class);

        Object parsedValue;
        Class<? extends Converter> converterClass = column.convert();
        //有自定义的转换器
        if (converterClass != EmptyConverter.class) {
            Converter converter = BeanUtils.instantiateClass(converterClass);
            try {
                converter.convert(rawValue);
            } catch (Exception e1) {
                throw new ExcelDataWrongException(e1.getMessage(), rawValue, column.headerName(), pos);
            }
        }
        //复用Web请求参数解析服务
        if (tClass != String.class) {
            if (conversionService.canConvert(String.class, tClass)) {
                try {
                    parsedValue = conversionService.convert(rawValue, tClass);
                } catch (ConversionException e1) {
                    //发生转换异常
                    throw new ExcelDataWrongException(e1.getMessage(), rawValue, column.headerName(), pos);
                }
            } else {
                log.info("无法转换[{}]为{}类型", rawValue, tClass.getCanonicalName());
                parsedValue = null;
            }
        } else parsedValue = rawValue;
        return parsedValue;
    }

    @Override
    public <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws Exception {
        Excel excel = targetClass.getAnnotation(Excel.class);
        if (excel == null) throw new RuntimeException(targetClass.getCanonicalName() + "没有配置@Excel注解!");

        Workbook wb = new XSSFWorkbook(inputStream);
        validExcel(wb, excel);

        Sheet sheet = wb.getSheetAt(excel.sheet());
        List<T> result = new ArrayList<>();
        Map<ColumnWrap, ExcelPos> mapping = null;

        for (int i = excel.offset(); i <= sheet.getLastRowNum(); i++) {
            //表头校验
            if (i == excel.offset()) {
                mapping = resolveMapping(sheet.getRow(i), targetClass);
                continue;
            }
            //处理数据
            Row row = sheet.getRow(i);
            T bean = targetClass.newInstance();
            boolean evictBlank = true;  //剔除全空行
            for (Map.Entry<ColumnWrap, ExcelPos> e : mapping.entrySet()) {
                ColumnWrap cw = e.getKey();
                Column column = cw.getColumn();
                ExcelPos pos = e.getValue();
                Class tClass = cw.getField().getType();

                Cell cell = row.getCell(pos.getColumnIndex());
                if (cell == null) continue; //代表单元格为空
                if (cell.getCellTypeEnum() == CellType.BLANK && skipBlank()) continue;
                cell.setCellType(CellType.STRING); //统一设置为string
                String rawValue = cell.getStringCellValue(); //单元格值
                Object parsedValue;
                ExcelPos dataPos = new ExcelPos(cell.getRowIndex(), cell.getColumnIndex());
                //转换数据
                parsedValue = convert(rawValue, cw, dataPos, tClass);
                Method setMethod = findMethod(targetClass, column.setter(), tClass);
                invokeMethod(setMethod, bean, parsedValue);
                evictBlank = false;
            }
            if (!evictBlank) result.add(bean);
        }
        return result;
    }

    @Override
    public <T> Workbook generateEmptyExcel(Class<T> targetClass) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Excel excel = targetClass.getAnnotation(Excel.class);
        String sheetName = excel.sheetName();
        Sheet sheet = wb.createSheet(sheetName.equals("") ? "sheet1" : sheetName);
        Row row = sheet.createRow(excel.offset());
        ExcelBeanMetaData excelBeanMetaData = getExcelBeanMeta(targetClass);
        ColumnWrap[] cws = excelBeanMetaData.getColumnWraps();
        for (int i = 0; i < cws.length; i++) {
            if (cws == null) continue;
            ColumnWrap cw = cws[i];
            Column c = cw.getColumn();
            Cell cell = row.createCell(i);
            cell.setCellValue(c.headerName());
        }
        return wb;
    }

    public <T> ExcelBeanMetaData getExcelBeanMeta(Class<T> targetClass) {
        ExcelBeanMetaData excelBeanMetaData = cache.get(targetClass);
        if (excelBeanMetaData == null) {
            excelBeanMetaData = resolveExcelBeanMeta(targetClass);
            cache.put(targetClass, excelBeanMetaData);
        }
        return excelBeanMetaData;
    }

    public <T> ExcelBeanMetaData resolveExcelBeanMeta(Class<T> targetClass) {
        Excel excel = targetClass.getAnnotation(Excel.class);
        Field[] fields = targetClass.getDeclaredFields();
        ColumnWrap[] cws = new ColumnWrap[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column c = f.getAnnotation(Column.class);
            if (c != null) {
                cws[i] = new ColumnWrap(c, f);
            }
            //else skip
        }
        ExcelBeanMetaData metaData = new ExcelBeanMetaData(excel, cws);
        cache.put(targetClass, metaData);
        return metaData;
    }

}
