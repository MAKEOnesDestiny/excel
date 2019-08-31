package com.zhou.demo.excel.factory;

import com.zhou.demo.excel.exception.ExcelDataWrongException;

public interface Callback<T> {

    Object doCallback(T t) throws ExcelDataWrongException;

}
