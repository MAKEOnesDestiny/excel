package com.zhou.demo.excel.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelPos{

    /**
     * 表格中的行序号(0,1,2,3....)
     */
    private Integer rowIndex;


    /**
     * 表格中的列序号(0,1,2,3....)
     */
    private Integer columnIndex;


}
