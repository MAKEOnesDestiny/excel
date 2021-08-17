package com.zhou.demo.excel.bean;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.Excel;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Excel(sheetName = "Sheet2")
@Data
public class BigData {

    @Column(headerName = "origin_order_id")
    private Long originOrderId;

    @Column(headerName = "platform_order_id")
    private String platformOrderId;

    @Column(headerName = "sort")
    private Integer sort;

    @Column(headerName = "platform")
    private String platform;

    @Column(headerName = "seller")
    private String seller;

    @Column(headerName = "platform_order_time")
    private Date platformOrderTime;

    @Column(headerName = "payment")
    private BigDecimal payment;

}
