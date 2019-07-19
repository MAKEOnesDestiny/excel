package com.zhou.demo.excel.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.Map;

public abstract class SelfAnnotationUtil {

    public static Map<String, Object> getMemberValuesMap(Annotation annotation) {
        Map<String, Object> result = null;

        //获取注解接口的代理对象中的InvocationHandler字段
        Field ihField = ReflectionUtils.findField(annotation.getClass(), "h", InvocationHandler.class);
        try {
            ihField.setAccessible(true);
            InvocationHandler ih = (InvocationHandler) ihField.get(annotation);
            Field field = ReflectionUtils.findField(ih.getClass(), "memberValues", Map.class);
            field.setAccessible(true);
            result = (Map<String, Object>) field.get(ih);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(ihField + "无法访问!");
        }
        return result;
    }

}
