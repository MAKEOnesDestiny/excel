package com.zhou.demo.excel.factory.formatter.impl;


import com.zhou.demo.excel.factory.formatter.Formatter;

public class DataTypeFormatter implements Formatter<String, String> {

    @Override
    public String format(String obj) {
        switch (obj) {
            case "QB":
                return "衍生指标";
            case "YS":
                return "衍生指标";
            case "DB":
                return "数据库指标";
            case "EB_YS":
                return "衍生指标";
            case "EB_DB":
                return "数据库指标";
            default:
                throw new RuntimeException("Unknow Type:" + obj);
        }
    }
}
