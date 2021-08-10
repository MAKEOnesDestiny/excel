package com.zhou.demo.excel.factory;


public class ExcelFactoryConfigInner implements ExcelFactoryConfig {

    /**
     * 是否忽略空白单元格，例如单元格格式设置为"文本"的空单元格
     * @return true->忽略  false->不忽略
     */
    private boolean skipBlank = false;

    /**
     * 是否捕获所有异常
     */
    private boolean catchAllException = true;

    /**
     * 是否过滤举例行
     */
    private boolean filterExample = true;

    /**
     * 是否使用1904时间格式
     */
    private boolean use1904 = false;

    public boolean isSkipBlank() {
        return skipBlank;
    }

    public void setSkipBlank(boolean skipBlank) {
        this.skipBlank = skipBlank;
    }

    public boolean isCatchAllException() {
        return catchAllException;
    }

    public void setCatchAllException(boolean catchAllException) {
        this.catchAllException = catchAllException;
    }

    public boolean isFilterExample() {
        return filterExample;
    }

    public void setFilterExample(boolean filterExample) {
        this.filterExample = filterExample;
    }

    @Override
    public boolean skipBlank() {
        return skipBlank;
    }

    public boolean isUse1904() {
        return use1904;
    }

    public void setUse1904(boolean use1904) {
        this.use1904 = use1904;
    }
}
