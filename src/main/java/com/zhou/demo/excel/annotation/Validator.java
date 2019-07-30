package com.zhou.demo.excel.annotation;

import com.zhou.demo.excel.factory.ExcelPos;

/**
 * 校验器必须有一个空构造器
 */
public interface Validator {

    /**
     * 在数据转换前进行校验
     *
     * @param rawValue
     * @param cw
     * @param pos
     * @param tClass
     * @param <T>
     * @return true:校验通过  false:校验失败
     * @throws Exception
     */
    default <T> boolean validBefore(String rawValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws Exception {
        return true;
    }

    /**
     * 在数据转换后进行校验
     *
     * @param convertedValue
     * @param cw
     * @param pos
     * @param tClass
     * @param <T>
     * @return true:校验通过  false:校验失败
     * @throws Exception
     */
    default <T> boolean validAfter(Object convertedValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws Exception {
        return true;
    }
}
