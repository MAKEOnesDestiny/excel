package com.zhou.demo.excel.xlsx;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ProcessInfo {

    private Integer rowIndex;
    private Integer columnIndex;

    //单元格原始的字符串信息
    private String raw;
    //key --> columnIndex
    private Map<Integer, CellData> map = new HashMap<>();
    //temp storage
    private CellData temp;

    public void resetMap() {
        map = new HashMap<>();
    }

}
