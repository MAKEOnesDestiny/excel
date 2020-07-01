package com.zhou.demo.excel.annotation.valid;

import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.config.ParamConst;
import com.zhou.demo.excel.config.ParameterPassHelp;

public class NumLimitValidator implements Validator {

    @Override
    public <T> boolean validBefore(String rawValue) {
        Integer integer = new Integer(rawValue);
        Integer limitNum = ParameterPassHelp.getParamByName(ParamConst.LENGTH_LIMIT_PARAM, Integer.class);
        if (integer > limitNum)
            throw new RuntimeException("数据不能大于" + limitNum);
        return true;
    }
}
