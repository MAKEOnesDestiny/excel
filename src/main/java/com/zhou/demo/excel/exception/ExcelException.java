package com.zhou.demo.excel.exception;

public abstract class ExcelException extends Exception{

    public ExcelException() {
    }

    public ExcelException(String message) {
        super(message);
    }
}
