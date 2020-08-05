package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class DefaultDynamicExcelHeaders extends AbstractDynamicExcelHeaders {

    public DefaultDynamicExcelHeaders(Integer headersRowNum, List<Header> headerList) {
        super(headersRowNum,headerList);
    }


}
