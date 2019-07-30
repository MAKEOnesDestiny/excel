package com.zhou.demo.excel.annotation.valid;

import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelPos;
import org.springframework.util.StringUtils;

public class NotEmptyValidator implements Validator {

    @Override
    public <T> boolean validBefore(String rawValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws Exception {
        if (StringUtils.isEmpty(rawValue))
            throw new ExcelDataWrongException("数据不能为空", rawValue, cw.getColumn().headerName(), pos);
        else return true;
    }

    @Override
    public <T> boolean validAfter(Object convertedValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws Exception {
        return true;
    }
}
