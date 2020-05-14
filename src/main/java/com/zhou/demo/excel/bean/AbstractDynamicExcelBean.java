package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDynamicExcelBean implements DynamicExcelBean {

//    private Map<DynamicExcelHeaders.Header, Cell> resolvedMap = new HashMap<>();

    private Map<DynamicExcelHeaders.Header, CellWrap> resolvedMap = new HashMap<>();

    @Override
    public Cell getCellByHeader(DynamicExcelHeaders.Header header) {
        return resolvedMap.get(header).getCell();
    }

    @Override
    public String getContentByHeader(DynamicExcelHeaders.Header header) {
        Cell cell = getCellByHeader(header);
        return cell == null ? null : cell.getStringCellValue();
    }

    @Override
    public Map<DynamicExcelHeaders.Header, CellWrap> getResolvedMap() {
        return resolvedMap;
    }
}
