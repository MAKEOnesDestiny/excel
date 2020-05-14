package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.annotation.Version;
import com.zhou.demo.excel.annotation.valid.NotEmptyValidator;
import com.zhou.demo.excel.factory.converter.TestConverter;
import lombok.Data;

import java.math.BigDecimal;

@Excel(sheetName = "商品映射维护模板导出")
@Data
public class TestBean {

    @Version({
            @Column(headerName = "价格", convert = TestConverter.class, version = 1)
    })
    @Column(headerName = "价格", convert = TestConverter.class, version = 1)
    private BigDecimal price;

    @Version({
            @Column(headerName = "平台", version = 1)
    })
    @Column(headerName = "平台", valid = {NotEmptyValidator.class})
    private String platform;

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

    public static void main(String[] args) {
        BigDecimal value = new BigDecimal("123");
        if(new BigDecimal(value.intValue()).compareTo(value) != 0){
            System.out.println(1);
        }

    }

}
