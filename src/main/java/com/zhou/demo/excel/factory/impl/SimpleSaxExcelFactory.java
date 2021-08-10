package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.bean.DataWrap;
import com.zhou.demo.excel.xlsx.AnalysisContext;
import com.zhou.demo.excel.xlsx.DefaultAnalysisContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class SimpleSaxExcelFactory extends SaxExcelFactory {

    public SimpleSaxExcelFactory() {
        super();
        AnalysisContext analysisContext = new DefaultAnalysisContext();
        setAnalysisContext(analysisContext);
    }

    @Override
    public <T> List<T> toBean(Workbook workbook, Class<T> targetClass) throws Exception {
        throw new UnsupportedOperationException("unsupported method");
    }

    @Override
    public <T> Map<Row, T> toBeanWithPos(Workbook workbook, Class<T> targetClass) throws Exception {
        throw new UnsupportedOperationException("unsupported method");
    }

    @Override
    public <T> List<DataWrap<T>> toWrapBean(Workbook workbook, Class<T> targetClass) throws Exception {
        throw new UnsupportedOperationException("unsupported method");
    }

    @Override
    public <T> Workbook generateExcel(List<T> list, Class<T> targetClass) throws IOException {
        return null;
    }

    @Override
    public <T> void generateSheet(List<T> list, Class<T> targetClass, Workbook wb) throws IOException {

    }

    @Override
    public boolean skipBlank() {
        return false;
    }
}
