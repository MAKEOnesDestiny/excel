package com.zhou.demo.excel.xlsx;

import com.zhou.demo.excel.annotation.ColumnWrap;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ProcessInfo {

    private Integer rowIndex;
    private Integer columnIndex;

    private Boolean headResolved = false;
    private Map<Integer, ColumnWrap> headName2Index;
    //单元格原始的字符串信息
    private String raw;
    //key --> columnIndex
    private Map<Integer, CellData> map = new HashMap<>();
    //temp storage
    private CellData temp;

    public void resetMap() {
        map = new HashMap<>();
    }

    public void headHasResolved() {
        headResolved = true;
    }

    public Boolean getHeadResolved() {
        return headResolved;
    }

    public Map<Integer, ColumnWrap> getHeadName2Index() {
        return headName2Index;
    }

    public void setHeadName2Index(Map<Integer, ColumnWrap> headName2Index) {
        this.headName2Index = headName2Index;
    }
}
