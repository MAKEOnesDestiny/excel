package com.zhou.demo.excel.annotation.valid;

import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.config.ParamConst;
import com.zhou.demo.excel.config.ParameterPassHelp;

public class LengthValidator implements Validator {

    @Override
    public <T> boolean validBefore(String rawValue) {
        Integer length = ParameterPassHelp.getParamByName(ParamConst.LENGTH_LIMIT_PARAM, Integer.class);
        if(rawValue!=null && rawValue.length()>length){
            throw new RuntimeException("长度不能超过"+length+"个字");
        }
        return true;
    }
}
