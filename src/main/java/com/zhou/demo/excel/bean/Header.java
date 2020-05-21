package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.converter.Converter;

public interface Header<T>{

    ExcelPos getHeaderPos();

    String getHeaderInStr();

    Class<T> getTargetClass();

    void setTargetClass(Class<T> targetClass);

    void setConverter(Class<? extends Converter> converter);

    Class<? extends Converter> getConverter();

    void setValidators(Class<? extends Validator> ... validators);

    Class<? extends Validator>[] getValidators();
}