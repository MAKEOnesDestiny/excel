package com.zhou.demo.excel.annotation.valid;

import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelPos;

public class DataRangeLimitValidator implements Validator {

    @Override
    public <T> boolean validBefore(String rawValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws Exception {
        if (!rawValue.equalsIgnoreCase("1") && !rawValue.equalsIgnoreCase("2"))
            throw new ExcelDataWrongException("数据只能取1或者2", rawValue, cw.getColumn().headerName(), pos);
        else return true;
    }

    @Override
    public <T> boolean validAfter(Object convertedValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws Exception {
        return true;
    }


}
