package com.zhou.demo.excel.factory;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.Assert;

@Data
@NoArgsConstructor
public class ExcelPos {

    /**
     * 表格中的行序号(0,1,2,3....)
     */
    private Integer rowIndex;

    /**
     * 表格中的列序号(0,1,2,3....)
     */
    private Integer columnIndex;

    private String sheetName;

    private Sheet sheet;

    public ExcelPos(Integer rowIndex, Integer columnIndex, String sheetName) {
        Assert.notNull(rowIndex, "rowIndex不能为null");
        Assert.notNull(columnIndex, "columnIndex不能为null");
        Assert.notNull(sheetName, "sheet不能为null");
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.sheetName = sheetName;
    }

    public ExcelPos(Integer rowIndex, Integer columnIndex, Sheet sheet) {
        Assert.notNull(rowIndex, "rowIndex不能为null");
        Assert.notNull(columnIndex, "columnIndex不能为null");
        Assert.notNull(sheet, "sheet不能为null");
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.sheet = sheet;
        this.sheetName = sheet.getSheetName();
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public String toString() {
        return "[row:" + (rowIndex + 1) + ",column:" + (columnIndex + 1) + "]";
    }

}
