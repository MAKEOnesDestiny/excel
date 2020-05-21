package com.zhou.demo.excel.bean;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.Assert;

public class CellWrap<T> {

    //根据cell转换解析得到的obj
    private T obj;

    //cell所在列对应的表头
    private Header header;

    private Cell cell;

    public CellWrap(T obj, Header header, Cell cell) {
        Assert.notNull(header,"header不能为null");
        Assert.notNull(header,"header不能为null");
        this.obj = obj;
        this.header = header;
        this.cell = cell;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
