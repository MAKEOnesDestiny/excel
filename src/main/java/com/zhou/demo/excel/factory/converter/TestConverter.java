package com.zhou.demo.excel.factory.converter;

import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

public class TestConverter implements Converter<String,Number> {

    @Override
    public Number convert(String source) {
        source = source.replaceAll(",","");
        return new BigDecimal(source);
    }
}
