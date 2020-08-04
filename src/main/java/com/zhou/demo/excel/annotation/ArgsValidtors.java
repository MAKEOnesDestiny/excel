package com.zhou.demo.excel.annotation;

import com.zhou.demo.excel.annotation.valid.NopValidator;

public @interface ArgsValidtors {

    Class<? extends Validator> validator() default NopValidator.class;

    String[] args() default {};

    Class<?>[] argsClass() default {};

}
