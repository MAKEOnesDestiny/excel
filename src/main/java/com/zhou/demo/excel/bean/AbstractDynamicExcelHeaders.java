package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.annotation.valid.NopValidator;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.converter.Converter;
import com.zhou.demo.excel.factory.converter.EmptyConverter;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

//not thread-safe class
public abstract class AbstractDynamicExcelHeaders implements DynamicExcelHeaders {

    private List<Header> headers;

    private final Integer headersRowNum;

    AbstractDynamicExcelHeaders(Integer headersRowNum) {
        Assert.notNull(headersRowNum, "headersRowNum不能为空");
        this.headersRowNum = headersRowNum;
    }

    @Override
    public List<Header> getHeaders() {
        return headers;
    }

    @Override
    public Integer getHeadersRowNum() {
        return headersRowNum;
    }

    //todo:
    @Override
    public List<String> getHeadersInStr() {
        return null;
    }

    @Override
    public List<Cell> getHeadersInCell() {
        return null;
    }

    @Override
    public Map<ExcelPos, String> getStrHeadersAsMap() {
        return null;
    }

    @Override
    public Map<ExcelPos, Cell> getCellHeadersAsMap() {
        return null;
    }


    public abstract class AbstractHeader<T> implements Header<T> {

        final Cell cell;

        private Class<T> targetClass;

        private Class<? extends Converter> converter = EmptyConverter.class;

        private Class<? extends Validator>[] validators = new Class[]{NopValidator.class};

        public AbstractHeader(Cell cell, Class<T> targetClass) {
            Assert.notNull(cell, "cell不能为null");
            this.cell = cell;
            if (targetClass == null) {
                targetClass = (Class<T>) String.class;
            }
            this.targetClass = targetClass;
        }

        public AbstractHeader(Cell cell) {
            Assert.notNull(cell, "cell不能为null");
            this.cell = cell;
            targetClass = (Class<T>) String.class;
        }

        @Override
        public void setTargetClass(Class<T> targetClass) {
            this.targetClass = targetClass;
        }

        @Override
        public Class<T> getTargetClass() {
            return targetClass;
        }

        public ExcelPos getExcelPos() {
            return new ExcelPos(cell.getRowIndex(),cell.getColumnIndex(),cell.getSheet());
        }

        @Override
        public void setConverter(Class<? extends Converter> converter) {
            this.converter = converter;
        }

        @Override
        public Class<? extends Converter> getConverter() {
            return converter;
        }

        @Override
        public Class<? extends Validator>[] getValidators() {
            return validators;
        }

        @Override
        public void setValidators(Class<? extends Validator>[] validators) {
            this.validators = validators;
        }

        //todo:
        @Override
        public ExcelPos getHeaderPos() {
            return null;
        }

        @Override
        public String getHeaderInStr() {
            return null;
        }
    }

}
