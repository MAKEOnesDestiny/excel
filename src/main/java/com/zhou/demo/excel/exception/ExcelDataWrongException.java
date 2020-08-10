package com.zhou.demo.excel.exception;

import com.zhou.demo.excel.factory.ExcelPos;
import lombok.Data;

/**
 * Excel数据错误异常，例如将"abc"传入Integer字段
 * 异常中包含1)数据的位置信息 2)异常数据
 */
@Data
public class ExcelDataWrongException extends ExcelException {

    private ExcelPos excelPos;

    //may be null
    private String columnName;

    private Object invalidData;

    public ExcelDataWrongException(String columnName, ExcelPos excelPos) {
        super();
        if(excelPos==null) throw new IllegalArgumentException("excelPos不能为空");
        this.excelPos = excelPos;
        this.columnName = columnName;
    }

    public ExcelDataWrongException(String message, Object invalidData, String columnName, ExcelPos excelPos) {
        super(message);
        if(excelPos==null) throw new IllegalArgumentException("excelPos不能为空");
        this.invalidData = invalidData;
        this.excelPos = excelPos;
        this.columnName = columnName;
    }

    public ExcelDataWrongException(String message, Object invalidData, ExcelPos excelPos) {
        super(message);
        if(excelPos==null) throw new IllegalArgumentException("excelPos不能为空");
        this.invalidData = invalidData;
        this.excelPos = excelPos;
        this.columnName = null;
    }

    @Override
    public String toString() {
        String result = "[" + excelPos.getSheet().getSheetName() + "]--->" + excelPos + "处数据错误,错误数据为:" + invalidData;
        if (getMessage() != null) result = result + "(" + getMessage() + ")";
        return result;
    }


}
