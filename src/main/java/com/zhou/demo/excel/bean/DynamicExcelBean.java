package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;

public interface DynamicExcelBean {

    Cell getCellByHeader(Header header);

    String getContentByHeader(Header header);

    Map<Header, CellWrap> getResolvedMap();

}
