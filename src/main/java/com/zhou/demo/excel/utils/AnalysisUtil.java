package com.zhou.demo.excel.utils;

import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.annotation.valid.NopValidator;
import com.zhou.demo.excel.factory.impl.DefaultExcelFactory.ValidPipeLine;
import org.springframework.beans.BeanUtils;

@SuppressWarnings("all")
public class AnalysisUtil {

    public static ValidPipeLine initPipeLine(Class[] validClass) {
        ValidPipeLine first = null;
        ValidPipeLine before = null;
        for (Class c : validClass) {
            if (c.getClass().equals(NopValidator.class)) {
                continue;
            }
            Validator validator = (Validator) BeanUtils.instantiateClass(c);
            ValidPipeLine validPipeLine = new ValidPipeLine(validator);
            validPipeLine.setPrev(before);
            if (first == null) {
                first = validPipeLine;
            }
            if (before != null) {
                before.setNext(validPipeLine);
            }
            before = validPipeLine;
        }
        return first;
    }

}
