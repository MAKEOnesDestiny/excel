package com.zhou.demo.excel.utils;

import java.lang.reflect.Field;

public abstract class BeanUtil {

    public static String findSetMethod(Field field) {
        return findSetMethod(field, false);
    }

    public static String findSetMethod(Field field, boolean check) {
        String fName = field.getName();
        String f = fName.substring(0, 1);
        //首字母变大写
        fName = "set" + fName.replaceFirst(f, f.toUpperCase());
        if (check) {
            Class clazz = field.getDeclaringClass();
            try {
                clazz.getDeclaredMethod(fName, field.getType());
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        return fName;
    }


}
