package com.zhou.demo.excel.factory;

import java.io.InputStream;
import java.util.List;

public interface ExcelFactory {

    <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws Exception;

}
