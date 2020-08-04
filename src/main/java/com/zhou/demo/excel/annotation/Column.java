package com.zhou.demo.excel.annotation;

import com.zhou.demo.excel.annotation.valid.NopValidator;
import com.zhou.demo.excel.factory.converter.Converter;
import com.zhou.demo.excel.factory.converter.EmptyConverter;
import com.zhou.demo.excel.factory.formatter.Formatter;
import com.zhou.demo.excel.factory.formatter.impl.DefaultFormatter;
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

    String getter() default "";

    /**
     * 是否必须存在
     */
    boolean required() default true;

    /**
     * 转换器
     */
    Class<? extends Converter> convert() default EmptyConverter.class;

    /**
     * 校验器 前置校验器的顺序即为数组中的顺序,越靠前的校验器越先被执行 后置校验器的顺序与前置校验器相反
     */
    Class<? extends Validator>[] valid() default {NopValidator.class};

    /**
     * 可以允许带参数的校验器
     */
    ArgsValidtors[] argsValid() default {};

    /**
     * 对数据进行格式化或者去除空格等标准化操作,生成excel时有效，上传excel时此字段无用
     */
    Class<? extends Formatter> format() default DefaultFormatter.class;

}
