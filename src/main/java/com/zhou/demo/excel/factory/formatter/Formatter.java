package com.zhou.demo.excel.factory.formatter;

public interface Formatter<S,T> {

    T format(S obj);

}
