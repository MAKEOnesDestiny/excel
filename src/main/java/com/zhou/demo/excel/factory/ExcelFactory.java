package com.zhou.demo.excel.factory;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ExcelFactory extends ExcelFactoryConfig {

    <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws Exception;

    <T> Workbook generateEmptyExcel(Class<T> targetClass) throws IOException;

}
