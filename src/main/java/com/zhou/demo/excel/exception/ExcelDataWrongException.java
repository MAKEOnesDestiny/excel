package com.zhou.demo.excel.exception;

import com.zhou.demo.excel.factory.ExcelPos;
import lombok.Data;

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
        return "[数据列:"+columnName+"]"+excelPos + "处数据错误,错误数据为:" + invalidData;
    }


}
