package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Row;

public class DataWrap<T> {

    private T data;

    private Row row;

    public DataWrap(T data, Row row) {
        this.data = data;
        this.row = row;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

}
