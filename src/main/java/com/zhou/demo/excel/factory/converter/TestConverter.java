package com.zhou.demo.excel.factory.converter;


import java.math.BigDecimal;

public class TestConverter implements Converter<String,Number> {

    public Number convert(String source) {
        source = source.replaceAll(",","");
        return new BigDecimal(source);
    }
}
