package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.factory.converter.TestConverter;
import lombok.Data;

import java.math.BigDecimal;

@Excel
@Data
public class TestBean {

    @Column(headerName = "价格",convert = TestConverter.class)
    private BigDecimal price;

    @Column(headerName = "平台")
    private int platform;

    @Column(headerName = "OMS商家编码"/*,setter = "setOmsBusinessCode"*/)
    private String omsBusinessCode;

    @Column(headerName = "OMS商品名称"/*,setter = "setOmsGoodsName"*/)
    private String omsGoodsName;

    @Column(headerName = "平台商品编码"/*,setter = "setPlatformGoodsCode"*/)
    private String platformGoodsCode;

    @Column(headerName = "平台商品名称"/*,setter = "setPlatformGoodsName"*/)
    private String platformGoodsName;

    @Column(headerName = "过期产品")
    private boolean deprecated;

}
