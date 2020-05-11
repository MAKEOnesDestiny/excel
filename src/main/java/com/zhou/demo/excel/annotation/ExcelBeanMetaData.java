package com.zhou.demo.excel.annotation;


public class ExcelBeanMetaData {

    private Excel excel;

    private ColumnWrap[] columnWraps;

    public ExcelBeanMetaData(Excel excel, ColumnWrap[] columnWraps) {
        this.excel = excel;
        this.columnWraps = columnWraps;
    }

    public ColumnWrap[] getColumnWraps() {
        return columnWraps;
    }

    public void setColumnWraps(ColumnWrap[] columnWraps) {
        this.columnWraps = columnWraps;
    }

    public Excel getExcel() {
        return excel;
    }

    public void setExcel(Excel excel) {
        this.excel = excel;
    }
}
