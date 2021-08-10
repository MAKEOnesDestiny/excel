package com.zhou.demo.excel.xlsx;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.annotation.ExcelBeanMetaData;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;

public class AnalysisInfo {

    public static final Map<Class, Object> cache = new HashMap<>();

    private Excel excel;
    private Class targetClass;
    private ExcelBeanMetaData excelBeanMetaData;

    public AnalysisInfo(Class targetClass) {
        this.targetClass = targetClass;
        Excel excel = (Excel) targetClass.getAnnotation(Excel.class);
        if (excel == null) {
            throw new RuntimeException(targetClass.getCanonicalName() + "没有配置@Excel注解!");
        }
        this.excel = excel;
        Field[] fields = targetClass.getDeclaredFields();
        int count = 0;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column c = f.getAnnotation(Column.class);
            if (c != null) {
                count++;
            }
            //else skip
        }
        ColumnWrap[] cws = new ColumnWrap[count];
        int j = 0;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column c = f.getAnnotation(Column.class);
            if (c != null) {
                cws[j] = new ColumnWrap(c, f, null);
                j++;
            }
            //else skip
        }
        ExcelBeanMetaData metaData = new ExcelBeanMetaData(excel, cws);
        this.excelBeanMetaData = metaData;
    }

    public String getSheetName() {
        return excel.sheetName();
    }

    public InputStream getSheetInputStream(SheetIterator sheetIterator) {
        if (!sheetIterator.hasNext()) {
            throw new RuntimeException("Can't find any sheet.Please check your file!");
        }
        String sheetName = excel.sheetName();
        Integer sheetIndex = excel.offset();
        boolean useName = false;
        if (getSheetName() != null) {
            useName = true;
        } else {
            useName = false;
        }
        Integer index = 0;

        InputStream found = null;
        while (sheetIterator.hasNext()) {
            InputStream temp = sheetIterator.next();
            if (useName) {
                String iteSheetName = sheetIterator.getSheetName();
                if (iteSheetName != null && iteSheetName.equals(sheetName)) {
                    found = temp;
                    break;
                }
            } else {
                if (index.equals(sheetIndex)) {
                    found = temp;
                    break;
                } else {
                    index++;
                }
            }
        }

        if (found == null) {
            throw new RuntimeException("Can't find specified sheet ==> " + excel);
        }
        return found;
    }

    public ExcelBeanMetaData getExcelBeanMetaData() {
        return excelBeanMetaData;
    }

    public Class getTargetClass() {
        return targetClass;
    }

}
