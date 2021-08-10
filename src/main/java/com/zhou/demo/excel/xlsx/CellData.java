package com.zhou.demo.excel.xlsx;

import java.math.BigDecimal;

public class CellData<T> {

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
}
