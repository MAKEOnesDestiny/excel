package com.zhou.demo.excel.factory;

import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.List;

public interface VersionExcelFactory extends ExcelFactory {

    /**
     * 多版本的Excel工厂
     * @param inputStream
     * @param targetClass
     * @param version
     * @param <T>
     * @return
     * @throws Exception
     * @see com.zhou.demo.excel.annotation.Version
     */
    <T> List<T> toBean(InputStream inputStream, Class<T> targetClass,int version) throws Exception;

    <T> List<T> toBean(Workbook workbook, Class<T> targetClass,int version) throws Exception;

    <T> Workbook generateExcel(List<T> list, Class<T> targetClass, int version) throws IOException;

    int getVersion();

}
