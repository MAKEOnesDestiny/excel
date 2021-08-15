package com.zhou.demo.excel.xlsx;

import java.math.BigDecimal;

public class CellData<T> {

    private Integer rowIndex;
    private Integer columnIndex;

    private CellTypeEnum cellTypeEnum;
    private String stringValue;
    private BigDecimal bigDecimalValue;
    private Boolean boolValue;

    private String dataFormat;
    private T data; //data to be converted

    public CellData() {
    }

    public CellData(CellTypeEnum cellTypeEnum) {
        this.cellTypeEnum = cellTypeEnum;
    }

    public CellData(Integer rowIndex, Integer columnIndex, CellTypeEnum cellTypeEnum) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.cellTypeEnum = cellTypeEnum;
    }

    public CellTypeEnum getCellTypeEnum() {
        return cellTypeEnum;
    }

    public void setCellTypeEnum(CellTypeEnum cellTypeEnum) {
        this.cellTypeEnum = cellTypeEnum;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }

    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getDataString() {
        if (bigDecimalValue != null) {
            return bigDecimalValue.toString();
        }
        if (boolValue != null) {
            return boolValue.toString();
        }
        return stringValue;
    }

}
