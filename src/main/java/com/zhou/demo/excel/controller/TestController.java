package com.zhou.demo.excel.controller;


import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.List;

@Controller
@RequestMapping("/test/controller")
public class TestController {

    @RequestMapping("/upload")
    @ResponseBody
    public Object upload(@RequestParam("zhf") boolean zhf, HttpServletRequest request) throws Exception {
        Part part = request.getPart("file");
        ExcelFactory factory = new SimpleExcelFactory(){
            @Override
            public boolean skipBlank() {
                return true;
            }
        };
        List list = null;
        try {
            list = factory.toBean(part.getInputStream(), TestBean.class);
        } catch (ExcelDataWrongException e) {
            return e.toString();
        }
        return list;
    }

}
