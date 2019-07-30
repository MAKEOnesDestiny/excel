package com.zhou.demo.excel.annotation;

import com.zhou.demo.excel.factory.impl.DefaultExcelFactory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

@Data
@AllArgsConstructor
public class ColumnWrap {

    private Column column;

    private Field field;

    private DefaultExcelFactory.ValidPipeLine pipeLine;


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ColumnWrap)) return false;
        ColumnWrap cw = (ColumnWrap) obj;
        return field.equals(cw.getField()) && column.equals(cw.getColumn());
    }

}
