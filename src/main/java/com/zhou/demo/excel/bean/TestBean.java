package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.annotation.ArgsValidtors;
import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.annotation.Version;
import com.zhou.demo.excel.annotation.valid.LengthLimitValidator;
import com.zhou.demo.excel.annotation.valid.LengthValidator;
import com.zhou.demo.excel.annotation.valid.NotEmptyValidator;
import com.zhou.demo.excel.factory.converter.TestConverter;
import java.math.BigDecimal;
import lombok.Data;

@Excel(sheetName = "商品映射维护")
@Data
public class TestBean {

    @Version({
                     @Column(headerName = "价格", convert = TestConverter.class, version = 1)
             })
    @Column(headerName = "价格", convert = TestConverter.class, version = 1, required = false)
    private BigDecimal price;

    @Version({
                     @Column(headerName = "平台", version = 1)
             })
    @Column(headerName = "平台", valid = {NotEmptyValidator.class, LengthValidator.class})
    private String platform;

    @Column(headerName = "OMS商家编码"/*,setter = "setOmsBusinessCode"*/)
    private String omsBusinessCode;

    @Column(headerName = "OMS商品名称"/*,setter = "setOmsGoodsName"*/)
    private String omsGoodsName;

    @Column(headerName = "平台商品ID", valid = {NotEmptyValidator.class})
    private String platformGoodsCode;

    @Column(headerName = "平台商品名称", argsValid = {@ArgsValidtors(validator = LengthLimitValidator.class, args = {"100"},
                                                               argsClass = {int.class})})
    private String platformGoodsName;

    @Column(headerName = "过期产品", required = false)
    private String deprecated;


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOmsBusinessCode() {
        return omsBusinessCode;
    }

    public void setOmsBusinessCode(String omsBusinessCode) {
        this.omsBusinessCode = omsBusinessCode;
    }

    public String getOmsGoodsName() {
        return omsGoodsName;
    }

    public void setOmsGoodsName(String omsGoodsName) {
        this.omsGoodsName = omsGoodsName;
    }

    public String getPlatformGoodsCode() {
        return platformGoodsCode;
    }

    public void setPlatformGoodsCode(String platformGoodsCode) {
        this.platformGoodsCode = platformGoodsCode;
    }

    public String getPlatformGoodsName() {
        return platformGoodsName;
    }

    public void setPlatformGoodsName(String platformGoodsName) {
        this.platformGoodsName = platformGoodsName;
    }

    public String getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(String deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "price=" + price +
                ", platform='" + platform + '\'' +
                ", omsBusinessCode='" + omsBusinessCode + '\'' +
                ", omsGoodsName='" + omsGoodsName + '\'' +
                ", platformGoodsCode='" + platformGoodsCode + '\'' +
                ", platformGoodsName='" + platformGoodsName + '\'' +
                ", deprecated='" + deprecated + '\'' +
                '}';
    }
}
