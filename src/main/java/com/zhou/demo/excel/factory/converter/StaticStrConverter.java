package com.zhou.demo.excel.factory.converter;

public class StaticStrConverter implements Converter<String, String> {

    @Override
    public String convert(String source) throws Exception {
        return "我是固定静态文字";
    }
}
