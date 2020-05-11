package com.zhou.demo.excel.annotation.valid;

import com.zhou.demo.excel.annotation.Validator;
import org.apache.commons.lang.StringUtils;

public class NotBlankValidator implements Validator {

    @Override
    public <T> boolean validBefore(String rawValue) {
        if (StringUtils.isBlank(rawValue)) {
            throw new RuntimeException("数据不能为空");
        }
        else {
            return true;
        }
    }
}
