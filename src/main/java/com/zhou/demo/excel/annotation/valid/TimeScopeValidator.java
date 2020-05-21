package com.zhou.demo.excel.annotation.valid;

import com.zhou.demo.excel.annotation.Validator;

public class TimeScopeValidator implements Validator {

    @Override
    public <T> boolean validBefore(String rawValue) {
        if(rawValue==null){
            return true;
        }
        if(!rawValue.equals("00:00-09:00") && !rawValue.equals("09:00-24:00") && !rawValue.equals("13:00-24:00")){
            throw new RuntimeException("请选择正确的时间范围");
        }else{
            return true;
        }
    }
}
