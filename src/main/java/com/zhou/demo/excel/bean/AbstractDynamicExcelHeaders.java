package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.factory.ExcelPos;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//not thread-safe class
public abstract class AbstractDynamicExcelHeaders implements DynamicExcelHeaders {

    private final List<Header> headers;

    private final Integer headersRowNum;

    AbstractDynamicExcelHeaders(Integer headersRowNum,List<Header> headers) {
        Assert.notNull(headersRowNum, "headersRowNum不能为空");
        this.headersRowNum = headersRowNum;
        this.headers = headers;
    }

    @Override
    public List<Header> getHeaders() {
        return headers;
    }

    @Override
    public Integer getHeadersRowNum() {
        return headersRowNum;
    }

    //todo:
    @Override
    public List<String> getHeadersInStr() {
        List<String> stringList = new ArrayList<>();
        if(headers==null){
            return stringList;
        }
        for (Header h:headers) {
            stringList.add(h.getHeaderInStr());
        }
        return stringList;
    }

    @Override
    public List<Cell> getHeadersInCell() {
        return null;
    }

    @Override
    public Map<ExcelPos, String> getStrHeadersAsMap() {
        return null;
    }

    @Override
    public Map<ExcelPos, Cell> getCellHeadersAsMap() {
        return null;
    }



}
