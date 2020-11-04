package com.zhou.demo.excel.test;

import java.util.HashMap;
import java.util.Map;

public class ParsedRow {

    private Integer rowIndex;

    private Map cellMap = new HashMap();

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Map getCellMap() {
        return cellMap;
    }

    public void setCellMap(Map cellMap) {
        this.cellMap = cellMap;
    }
}
