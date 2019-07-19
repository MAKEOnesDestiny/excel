package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.Excel;
import lombok.Data;

@Excel
@Data
public class TestBean {

    @Column(headerName = "平台"/*,setter = "setPlatform"*/)
    private Integer platform;

    @Column(headerName = "OMS商家编码"/*,setter = "setOmsBusinessCode"*/)
    private String omsBusinessCode;

    @Column(headerName = "OMS商品名称"/*,setter = "setOmsGoodsName"*/)
    private String omsGoodsName;

    @Column(headerName = "平台商品编码"/*,setter = "setPlatformGoodsCode"*/)
    private String platformGoodsCode;

    @Column(headerName = "平台商品名称"/*,setter = "setPlatformGoodsName"*/)
    private String platformGoodsName;

}
