package com.zhou.demo.excel.annotation;

import com.zhou.demo.excel.annotation.valid.NopValidator;
import com.zhou.demo.excel.factory.converter.EmptyConverter;
import org.springframework.core.convert.converter.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 多版本控制
     */
    int version() default -1;

    int position() default -1;

    /**
     * 对应列的表头的名称
     */
    String headerName();

    String setter() default "";

    /**
     * 转换器
     */
    Class<? extends Converter> convert() default EmptyConverter.class;

    /**
     * 校验器
     */
    Class<? extends Validator>[] valid() default {NopValidator.class};

}
