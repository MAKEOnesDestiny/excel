package com.zhou.demo.excel.utils;

import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.VersionExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import com.zhou.demo.excel.factory.impl.VersionExcelImplFactory;

public abstract class ExcelFactories {

    private static final ExcelFactory SIMPLE_EXCEL = new SimpleExcelFactory();

    private static final VersionExcelFactory VERSION_EXCEL = new VersionExcelImplFactory();

    public static final ExcelFactory simpleExcel() {
        return SIMPLE_EXCEL;
    }

    public static final VersionExcelFactory versionExcel() {
        return VERSION_EXCEL;
    }

}
