package com.zhou.demo.excel.utils;

import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.bean.DataWrap;
import com.zhou.demo.excel.factory.ExcelPos;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class ExcelDataUtil {

    public static final <T> List<T> extractAllDataFromWrapData(List<DataWrap<T>> dataWrapList, Class<T> targetClass) {
        final List<T> result = new ArrayList<>();
        if (dataWrapList == null || dataWrapList.size() == 0) {
            return result;
        }
        dataWrapList.forEach((t) -> {
            if (t != null) {
                result.add(t.getData());
            }
        });
        return result;
    }

    /**
     * 一个非常简单判断举例列的方法，当一行中第一个cell是以'例'开头，那么将这一行全部清空(但是保留此行)
     *
     * @param wb
     * @param sheetName
     */
    public static final void simpleClearExampleRow(Workbook wb, String sheetName) {
        Sheet sheet = wb.getSheet(sheetName);
        if (sheet == null) {
            return;
        }
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            if (cell != null && cell.getStringCellValue() != null && cell.getStringCellValue().startsWith("例")) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    if (row.getCell(j) != null) {
                        row.getCell(j).setCellValue((String) null);
                    }
                }
            }
        }
    }

    /**
     * 一个非常简单判断举例列的方法，一行中每一个cell都是是以'例'开头
     */
    public static final boolean isExampleRow(Row row, Map<ColumnWrap, ExcelPos> mapping) {
        if (row == null || mapping.size() == 0) {
            return false;
        }
        for (Map.Entry<ColumnWrap, ExcelPos> e : mapping.entrySet()) {
            ExcelPos pos = e.getValue();
            Cell cell = row.getCell(pos.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell == null) {
                return false;
            }
            if(cell.getStringCellValue()==null || !cell.getStringCellValue().startsWith("例")){
                return false;
            }
        }
        return true;
    }

}
