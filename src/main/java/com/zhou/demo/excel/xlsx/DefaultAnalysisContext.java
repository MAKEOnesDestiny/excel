package com.zhou.demo.excel.xlsx;

import com.zhou.demo.excel.factory.ExcelFactoryConfigInner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.model.StylesTable;

@SuppressWarnings("all")
public class DefaultAnalysisContext<T> implements AnalysisContext<T> {

    private Map<Integer, Object> sharingStrings;
    private StylesTable stylesTable;
    private ExcelFactoryConfigInner configInner;
    private AnalysisInfo analysisInfo;
    private ProcessInfo processInfo;
    private volatile boolean initialized; //has initialized yet?
    private final List<Exception> exceptions = new ArrayList<>();

    private List<T> results = new ArrayList<>();
    private List<Map<Integer, CellData>> tempResults = new ArrayList<>();

    @Override
    public Map<Integer, Object> sharingStrings() {
        return sharingStrings;
    }

    @Override
    public StylesTable stylesTable() {
        return stylesTable;
    }

    @Override
    public List<T> results() {
        return results;
    }

    @Override
    public List<Map<Integer, CellData>> tempResults() {
        return tempResults;
    }

    @Override
    public ExcelFactoryConfigInner getConfig() {
        return configInner;
    }

    //存放具体业务相关的信息
    @Override
    public AnalysisInfo analysisInfo() {
        return analysisInfo;
    }

    @Override
    public ProcessInfo processInfo() {
        return processInfo;
    }

    @Override
    public void setProcessInfo(ProcessInfo processInfo) {
        this.processInfo = processInfo;
    }

    public void setConfigInner(ExcelFactoryConfigInner configInner) {
        this.configInner = configInner;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public List<Exception> processExceptions() {
        return exceptions;
    }

    @Override
    public void initContext(RawContextInfo rawContextInfo) {
        Class targetClass = rawContextInfo.getTargetClass();
        analysisInfo = new AnalysisInfo(targetClass);
        configInner = new ExcelFactoryConfigInner();
        initialized = true;
    }

    @Override
    public void initContext(Map<Integer, Object> sharingStrings, StylesTable stylesTable) {
        this.sharingStrings = sharingStrings;
        this.stylesTable = stylesTable;
    }
}
