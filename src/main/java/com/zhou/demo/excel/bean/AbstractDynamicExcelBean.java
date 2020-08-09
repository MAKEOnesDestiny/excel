package com.zhou.demo.excel.bean;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;

public abstract class AbstractDynamicExcelBean implements DynamicExcelBean {

    private Map<Header, CellWrap> resolvedMap = new HashMap<>();

    @Override
    public Cell getCellByHeader(Header header) {
        CellWrap cw = resolvedMap.get(header);
        return cw == null ? null : cw.getCell();
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

    @Override
    public String toString() {
        Map<String,Object> map = new HashMap<>();
        for (Map.Entry<Header, CellWrap> e:resolvedMap.entrySet()) {
            map.put(e.getKey().getHeaderInStr(),e.getValue().getObj());
        }
        return map.toString();
    }
}
