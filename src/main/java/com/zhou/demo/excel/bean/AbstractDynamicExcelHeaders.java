package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.factory.ExcelPos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.Assert;

//not thread-safe class
public abstract class AbstractDynamicExcelHeaders implements DynamicExcelHeaders {

    private final List<Header> headers;

    private final Integer headersRowNum;

    AbstractDynamicExcelHeaders(Integer headersRowNum, List<Header> headers) {
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

    @Override
    public List<String> getHeadersInStr() {
        List<String> stringList = new ArrayList<>();
        if (headers == null) {
            return stringList;
        }
        for (Header h : headers) {
            stringList.add(h.getHeaderInStr());
        }
        return stringList;
    }

    @Override
    public List<Cell> getHeadersInCell() {
        List<Cell> cellList = new ArrayList<>();
        if (headers == null) {
            return cellList;
        }
        for (Header h : headers) {
            ExcelPos pos = h.getHeaderPos();
            Sheet sheet = pos.getSheet();
            cellList.add(sheet.getRow(pos.getRowIndex()).getCell(pos.getColumnIndex()));
        }
        return cellList;
    }

    @Override
    public Map<ExcelPos, String> getStrHeadersAsMap() {
        Map<ExcelPos, String> map = new HashMap<>();
        if (headers == null) {
            return map;
        }
        for (Header h : headers) {
            ExcelPos pos = h.getHeaderPos();
            map.put(pos, h.getHeaderInStr());
        }
        return map;
    }

    @Override
    public Map<ExcelPos, Cell> getCellHeadersAsMap() {
        Map<ExcelPos, Cell> map = new HashMap<>();
        if (headers == null) {
            return map;
        }
        for (Header h : headers) {
            ExcelPos pos = h.getHeaderPos();
            Sheet sheet = pos.getSheet();
            map.put(pos, sheet.getRow(pos.getRowIndex()).getCell(pos.getColumnIndex()));
        }
        return map;
    }


}
