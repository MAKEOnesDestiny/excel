package com.zhou.demo.excel.factory;

import com.zhou.demo.excel.bean.DataWrap;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelFactory extends ExcelFactoryConfig {

    ExcelFactoryConfigInner getConfig();

    void setConfig(ExcelFactoryConfigInner excelFactoryConfigInner);

    <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws Exception;

    //提供对workbook的解析方法,避免每次对流的解析,降低处理时间
    <T> List<T> toBean(Workbook workbook, Class<T> targetClass) throws Exception;

    <T> Map<Row, T> toBeanWithPos(Workbook workbook, Class<T> targetClass) throws Exception;

    <T> List<DataWrap<T>> toWrapBean(Workbook workbook, Class<T> targetClass) throws Exception;

    <T> Workbook generateExcel(List<T> list, Class<T> targetClass) throws IOException;

    <T> void generateSheet(List<T> list, Class<T> targetClass, Workbook wb) throws IOException;

}
