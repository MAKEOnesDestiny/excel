package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.converter.Converter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;

public interface DynamicExcelHeaders {

    List<String> getHeadersInStr();

    List<Cell> getHeadersInCell();

    List<Header> getHeaders();

    Map<ExcelPos,String> getStrHeadersAsMap();

    Map<ExcelPos,Cell> getCellHeadersAsMap();

    Integer getHeadersRowNum();



}
