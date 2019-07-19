package com.zhou.demo.excel.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    /**
     * excel表头的偏移值
     */
    int offset() default 0;

    /**
     * excel表格的sheet编号，默认为第一个(0)
     */
    int sheet() default 0;

    /**
     * sheet名称
     */
    String sheetName() default "";

}
