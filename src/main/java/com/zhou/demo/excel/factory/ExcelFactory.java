package com.zhou.demo.excel.factory;

import com.zhou.demo.excel.annotation.Excel;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ExcelFactory extends ExcelFactoryConfig {

    <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws Exception;

    //提供对workbook的解析方法,避免每次对流的解析,降低处理时间
    <T> List<T> toBean(Workbook workbook, Class<T> targetClass) throws Exception;

    <T> Workbook generateEmptyExcel(Class<T> targetClass) throws IOException;

//    void validExcel(Workbook workBook, Excel excel) throws Exception;

}
