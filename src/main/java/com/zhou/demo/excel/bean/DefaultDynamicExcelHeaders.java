package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Cell;

public class DefaultDynamicExcelHeaders extends AbstractDynamicExcelHeaders {

    DefaultDynamicExcelHeaders(Integer headersRowNum) {
        super(headersRowNum);
    }


    public class DefaultHeader<T> extends AbstractDynamicExcelHeaders.AbstractHeader<T> {

        public DefaultHeader(Cell cell, Class targetClass) {
            super(cell, targetClass);
        }

        public DefaultHeader(Cell cell) {
            super(cell);
        }

    }


}
