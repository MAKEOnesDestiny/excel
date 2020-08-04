package com.zhou.demo.excel.annotation.valid;

import com.zhou.demo.excel.annotation.Validator;

public class LengthLimitValidator implements Validator {

    private int length;

    public LengthLimitValidator(int length) {
        this.length = length;
    }

    @Override
    public <T> boolean validBefore(String rawValue) {
        if (rawValue != null && rawValue.length() > length) {
            throw new RuntimeException("长度不能超过" + length + "个字");
        }
        return true;
    }
}
