package com.zhou.demo.excel.controller;


import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.List;

@Controller
@RequestMapping("/test/controller")
public class TestController {

    @RequestMapping("/upload")
    public Object upload(@RequestParam("zhf") boolean zhf, HttpServletRequest request) throws Exception {
        Part part = request.getPart("file");
        ExcelFactory factory = new SimpleExcelFactory();
        List list = factory.toBean(part.getInputStream(), TestBean.class);
        return new Object();
    }

}
