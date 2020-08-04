package com.zhou.demo.excel.utils;

public abstract class ExceptionUtil {

    /**
     * @param ex
     * @return like
     *      com.zhou.springboot.exceptions.ExceptionToString.main(ExceptionToString.java:7)
     *      com.zhou.springboot.exceptions.ExceptionToString.main(ExceptionToString.java:7)
     *      com.zhou.springboot.exceptions.ExceptionToString.main(ExceptionToString.java:7)
     */
    public static String getStackTraceString(Throwable ex) {
        StackTraceElement[] traceElements = ex.getStackTrace();
        StringBuilder traceBuilder = new StringBuilder();
        traceBuilder.append(ex.getMessage()).append("\n");
        if (traceElements != null && traceElements.length > 0) {
            for (StackTraceElement traceElement : traceElements) {
                traceBuilder.append(traceElement.toString());
                traceBuilder.append("\n");
            }
        }
        return traceBuilder.toString();
    }

}
