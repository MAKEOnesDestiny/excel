package com.zhou.demo.excel.utils;

import com.zhou.demo.excel.bean.DataWrap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

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
        if(sheet==null){
            return ;
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

}
