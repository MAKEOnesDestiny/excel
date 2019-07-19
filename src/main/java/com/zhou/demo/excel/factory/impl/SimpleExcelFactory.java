package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.config.ApplicationContextAccessor;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.ExcelPos;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.convert.ConversionService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhou.demo.excel.utils.BeanUtil.findSetMethod;
import static com.zhou.demo.excel.utils.SelfAnnotationUtil.getMemberValuesMap;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

@Log4j2
public class SimpleExcelFactory implements ExcelFactory {

    public ExcelPos findPos(String s, Row row) {
        short fNum = row.getFirstCellNum();
        short lNum = row.getLastCellNum();
        if (fNum < 0 || lNum < 0) return null;
        for (int i = fNum; i <= lNum; i++) {
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
            ExcelPos pos = null;
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
        return map;
    }


    @Override
    public <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Excel excel = targetClass.getAnnotation(Excel.class);
        if (excel == null) throw new RuntimeException(targetClass.getCanonicalName() + "没有配置@Excel注解!");

        Workbook wb = new XSSFWorkbook(inputStream);
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
            for (Map.Entry<ColumnWrap, ExcelPos> e : mapping.entrySet()) {
                ColumnWrap cw = e.getKey();
                Column column = cw.getColumn();
                ExcelPos pos = e.getValue();
                Cell cell = row.getCell(pos.getColumnIndex());
                if (cell == null) continue; //代表单元格为空
                cell.setCellType(CellType.STRING); //同一设置为string
                String rawValue = cell.getStringCellValue(); //单元格值
                Object parsedValue = null;

                //复用Web请求参数解析服务
                ConversionService conversionService = ApplicationContextAccessor.getApplicationContext().getBean(ConversionService.class);
                Class tClass = cw.getField().getType();
                if (tClass != String.class) {
                    if (conversionService.canConvert(String.class, tClass)) {
                        parsedValue = conversionService.convert(rawValue, tClass);
                    } else {
                        log.info("无法转换[{}]为{}类型", rawValue, tClass.getCanonicalName());
                        continue;
                    }
                } else parsedValue = rawValue;
                //todo:参数转换
                Method setMethod = findMethod(targetClass, column.setter(), tClass);
                invokeMethod(setMethod, bean, parsedValue);
            }
            result.add(bean);
        }
        return result;
    }


}
