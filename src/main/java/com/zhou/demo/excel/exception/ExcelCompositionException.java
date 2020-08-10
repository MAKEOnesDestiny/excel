package com.zhou.demo.excel.exception;

import java.util.List;

/**
 * Excel组合异常类，其内部存放多个异常
 */
public class ExcelCompositionException extends ExcelException {

    private final List<ExcelDataWrongException> exceptions;

    public ExcelCompositionException(List<ExcelDataWrongException> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (ExcelDataWrongException e : exceptions) {
            if (!first) {
                sb.append("\r\n");
            }
            sb.append(e.toString());
            first = false;
        }
        return sb.toString();
    }
}
