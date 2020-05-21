package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Cell;

public class DefaultHeader<T> extends AbstractHeader<T> {

    public DefaultHeader(Cell cell, Class targetClass) {
        super(cell, targetClass);
    }

    public DefaultHeader(Cell cell) {
        super(cell);
    }

}
