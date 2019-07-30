package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.annotation.*;
import com.zhou.demo.excel.annotation.valid.NopValidator;
import com.zhou.demo.excel.config.ApplicationContextAccessor;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.converter.EmptyConverter;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zhou.demo.excel.utils.BeanUtil.findSetMethod;
import static com.zhou.demo.excel.utils.SelfAnnotationUtil.getMemberValuesMap;

@Log4j2
public class SimpleExcelFactory extends DefaultExcelFactory {

    protected Sheet getSheet(Excel excel, Workbook workbook) {
        String sName = excel.sheetName();
        Sheet sheet;
        if (!sName.equals("")) {
            sheet = workbook.getSheet(sName);
        } else {
            sheet = super.getSheet(excel, workbook);
        }
        return sheet;
    }

    protected ExcelPos findPos(String s, Row row) {
        short fNum = row.getFirstCellNum();
        short lNum = row.getLastCellNum();
        if (fNum < 0 || lNum < 0) return null;
        for (int i = fNum; i < lNum; i++) {
            Cell cell = row.getCell(i);
            //todo:添加表头校验
            String value = cell.getStringCellValue();
            if (s.equals(value)) return new ExcelPos(row.getRowNum(), i, row.getSheet());
        }
        return null;
    }

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
                Class[] validClass = c.valid();
                ValidPipeLine validPipeLine = initPipeLine(validClass);
                //todo:添加全局缓存避免重复解析
                map.put(new ColumnWrap(c, f, validPipeLine), pos);
            }
            //else skip
        }
//        cache.put(clazz, map);
        return map;
    }

    private ValidPipeLine initPipeLine(Class[] validClass) {
        ValidPipeLine first = null;
        ValidPipeLine before = null;
        for (Class c : validClass) {
            if (c.getClass().equals(NopValidator.class)) continue;
            Validator validator = (Validator) BeanUtils.instantiateClass(c);
            ValidPipeLine validPipeLine = new ValidPipeLine(validator);
            validPipeLine.setPrev(before);
            if (first == null) first = validPipeLine;
            if (before != null) before.setNext(validPipeLine);
            before = validPipeLine;
        }
        return first;
    }

    @Override
    public final <T> Object convert(String rawValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws ExcelDataWrongException {
        Column column = cw.getColumn();
        ConversionService conversionService = ApplicationContextAccessor.getApplicationContext().getBean(ConversionService.class);
        Class<? extends Converter> converterClass = column.convert();
        //有自定义的转换器
        if (converterClass != EmptyConverter.class) {
            Converter converter = BeanUtils.instantiateClass(converterClass);
            try {
                return converter.convert(rawValue);
            } catch (Exception e1) {
                throw new ExcelDataWrongException(e1.getMessage(), rawValue, column.headerName(), pos);
            }
        }
        Object parsedValue;
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


    private final static Map<Class, ExcelBeanMetaData> cache = new ConcurrentHashMap<>();

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
                cws[i] = new ColumnWrap(c, f, null);
            }
            //else skip
        }
        ExcelBeanMetaData metaData = new ExcelBeanMetaData(excel, cws);
        cache.put(targetClass, metaData);
        return metaData;
    }

}
