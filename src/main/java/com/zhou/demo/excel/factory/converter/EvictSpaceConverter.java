package com.zhou.demo.excel.factory.converter;


public class EvictSpaceConverter implements Converter {

    //去除字符串中的所有空格
    public Object convert(Object source) throws Exception {
        if(source instanceof String){
            return ((String) source).replaceAll(" ","");
        }
        return source;
    }
}
