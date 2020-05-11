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
     * @param <T>
     * @return true:校验通过  false:校验失败
     * @throws Exception
     */
    default <T> boolean validBefore(String rawValue) throws Exception {
        return true;
    }

    /**
     * 在数据转换后进行校验
     *
     * @param convertedValue
     * @param <T>
     * @return true:校验通过  false:校验失败
     * @throws Exception
     */
    default <T> boolean validAfter(Object convertedValue) throws Exception {
        return true;
    }
}
