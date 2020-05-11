package com.zhou.demo.excel.annotation.valid;


import com.zhou.demo.excel.annotation.Validator;

public class PostiveIntegerValidator implements Validator {

    @Override
    public <T> boolean validBefore(String rawValue) throws Exception {
        //允许为空
        if (rawValue == null || rawValue.equals(""))
            return true;
        else {
            try {
                Integer integer = new Integer(rawValue);
                if (!(integer.compareTo(0) > 0)) {
                    throw new RuntimeException("数据必须为正整数");
                }
            } catch (Exception e) {
                throw new RuntimeException("数据必须为正整数");
            }
            return true;
        }
    }

}
