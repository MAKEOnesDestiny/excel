package com.zhou.demo.excel.xlsx;

import com.zhou.demo.excel.factory.ExcelFactoryConfigInner;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.model.StylesTable;

public interface AnalysisContext<T> {

    Map<Integer, Object> sharingStrings();

    StylesTable stylesTable();

    List<T> results();

    //mainly config
    ExcelFactoryConfigInner getConfig();

    //input analysis info
    AnalysisInfo analysisInfo();

    //info when processing
    ProcessInfo processInfo();

    void setProcessInfo(ProcessInfo processInfo);

    void setConfigInner(ExcelFactoryConfigInner configInner);

    boolean isInitialized();

    //init static analysis context,for example, targetClass & config info
    void initContext(RawContextInfo rawContextInfo);

    void initContext(Map<Integer, Object> sharingStrings, StylesTable stylesTable);
}
