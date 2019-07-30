package com.zhou.demo.excel.exception;

import com.zhou.demo.excel.factory.ExcelPos;
import lombok.Data;

/**
 * Excel数据错误异常，例如将"abc"传入Integer字段
 * 异常中包含1)数据的位置信息 2)异常数据
 */
@Data
public class ExcelDataWrongException extends Exception {

    private ExcelPos excelPos;

    private String columnName;

    private Object invalidData;

    public ExcelDataWrongException(String columnName, ExcelPos excelPos) {
        super();
        this.excelPos = excelPos;
        this.columnName = columnName;
    }

    public ExcelDataWrongException(String message, String columnName, ExcelPos excelPos) {
        super(message);
        this.excelPos = excelPos;
        this.columnName = columnName;
    }

    public ExcelDataWrongException(String message, Object invalidData, String columnName, ExcelPos excelPos) {
        super(message);
        this.invalidData = invalidData;
        this.excelPos = excelPos;
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        String result = "[" + excelPos.getSheet().getSheetName() + "]--->" + excelPos + "处数据错误,错误数据为:" + invalidData;
        if (getMessage() != null) result = result + "(" + getMessage() + ")";
        return result;
    }


}
