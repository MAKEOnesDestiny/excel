package com.zhou.demo.excel.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see ExcelBeanArguementResolver
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelToBean {

    /**
     * 目标BEAN
     */
    Class targetClass();

    /**
     * 版本号，如果是无版本的则不用填
     */
    int version() default -1;

    /**
     * 请求中的文件名称
     */
    String file();

}
