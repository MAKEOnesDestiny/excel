package com.zhou.demo.excel.exception;

/**
 * Excel框架错误
 */
public class ExcelFrameException extends RuntimeException {

    public ExcelFrameException(){
        super();
    }

    public ExcelFrameException(String message) {
        super(message);
    }

}
