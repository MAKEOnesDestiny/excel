package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;

public interface DynamicExcelBean {

    Cell getCellByHeader(DynamicExcelHeaders.Header header);

    String getContentByHeader(DynamicExcelHeaders.Header header);

    Map<DynamicExcelHeaders.Header, CellWrap> getResolvedMap();

}
