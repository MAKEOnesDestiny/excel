package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDynamicExcelBean implements DynamicExcelBean {

    private Map<Header, CellWrap> resolvedMap = new HashMap<>();

    @Override
    public Cell getCellByHeader(Header header) {
        return resolvedMap.get(header).getCell();
    }

    @Override
    public String getContentByHeader(Header header) {
        Cell cell = getCellByHeader(header);
        return cell == null ? null : cell.getStringCellValue();
    }

    @Override
    public Map<Header, CellWrap> getResolvedMap() {
        return resolvedMap;
    }
}
