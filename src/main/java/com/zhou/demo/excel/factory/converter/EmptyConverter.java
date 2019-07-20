package com.zhou.demo.excel.factory.converter;

import org.springframework.core.convert.converter.Converter;

public class EmptyConverter implements Converter {
    @Override
    public Object convert(Object source) {
        return null;
    }
}
