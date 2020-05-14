package com.zhou.demo.excel.bean;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

public class TestBeanChild extends TestBean {

    public  String child;

    public static void main(String[] args) {
        Field[] list = TestBeanChild.class.getDeclaredFields();
        
        System.out.println();
    }

}
