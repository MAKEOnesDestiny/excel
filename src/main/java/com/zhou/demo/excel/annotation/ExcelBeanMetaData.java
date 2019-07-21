package com.zhou.demo.excel.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcelBeanMetaData {

    private Excel excel;

    private ColumnWrap[] columnWraps;

}
