package com.zhou.demo.excel.annotation;

import com.zhou.demo.excel.factory.impl.DefaultExcelFactory;

import java.lang.reflect.Field;

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

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public DefaultExcelFactory.ValidPipeLine getPipeLine() {
        return pipeLine;
    }

    public void setPipeLine(DefaultExcelFactory.ValidPipeLine pipeLine) {
        this.pipeLine = pipeLine;
    }

    public ColumnWrap(Column column, Field field, DefaultExcelFactory.ValidPipeLine pipeLine) {
        this.column = column;
        this.field = field;
        this.pipeLine = pipeLine;
    }
}
