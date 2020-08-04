package com.zhou.demo.excel.factory;

import com.zhou.demo.excel.bean.DynamicExcelBean;
import com.zhou.demo.excel.bean.DynamicExcelHeaders;
import org.apache.poi.ss.usermodel.Sheet;


import java.util.List;

public interface DynamicExcelFactory extends ExcelFactoryConfig{

    ExcelFactoryConfigInner getConfig();

    void setConfig(ExcelFactoryConfigInner excelFactoryConfigInner);

    List<DynamicExcelBean> toDynamicBean(Sheet sheet,DynamicExcelHeaders headers) throws Exception;

    /**
     * @param sheet
     * @param headerPosition 表头所在行位置 0,1,2,3....
     * @return
     */
    DynamicExcelHeaders getHeadersFromExcel(Sheet sheet,Integer headerPosition);

}
