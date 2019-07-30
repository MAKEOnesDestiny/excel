package com.zhou.demo.excel.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;

@Data
//@AllArgsConstructor
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

    /**
     * sheet名称
     */
//    private String sheetName;

    private Sheet sheet;

/*    public ExcelPos(Integer rowIndex, Integer columnIndex, String sheetName) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.sheetName = sheetName;
    }*/

    public ExcelPos(Integer rowIndex, Integer columnIndex, Sheet sheet) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.sheet = sheet;
    }

    @Override
    public String toString() {
        return "[row:" + (rowIndex + 1) + ",column:" + (columnIndex + 1) + "]";
    }

}
