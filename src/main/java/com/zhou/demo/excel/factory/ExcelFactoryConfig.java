package com.zhou.demo.excel.factory;

public interface ExcelFactoryConfig {

    /**
     * 是否忽略空白单元格，例如单元格格式设置为"文本"的空单元格
     * @return true->忽略  false->不忽略
     */
    boolean skipBlank();

    default boolean catchAllException(){return false;};

}
