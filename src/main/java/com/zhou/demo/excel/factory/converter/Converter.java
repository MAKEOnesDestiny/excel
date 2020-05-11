package com.zhou.demo.excel.factory.converter;

public interface Converter<S, T> {

    T convert(S source) throws Exception;

}
