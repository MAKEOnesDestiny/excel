package com.zhou.demo.excel.controller;


import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Vector;

@Controller
@RequestMapping("/test/controller")
@Log4j2
public class TestController {

    @RequestMapping("/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request) throws Exception {
        Part part = request.getPart("file");
        ExcelFactory factory = new SimpleExcelFactory() {
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

    @RequestMapping("/export")
    public void export(@RequestParam("bean") String bean, HttpServletResponse response) throws Exception {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        String classInStr = "com.zhou.demo.excel.bean." + bean;
        Class clazz = Class.forName(classInStr);
        if (clazz == null) throw new RuntimeException("错误的参数:" + bean);
        log.info("找到class:" + clazz.getCanonicalName());
        ExcelFactory factory = new SimpleExcelFactory();
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=file.xls");
        OutputStream os = response.getOutputStream();
        factory.generateEmptyExcel(clazz).write(os);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        /*ClassLoader cl = TestBean.class.getClassLoader();
        Field f = ReflectionUtils.findField(cl.getClass(), "classes");
        f.setAccessible(true);
        Vector<Class> o = (Vector) ReflectionUtils.getField(f, cl);
        for (Class c:o) {
            if(c.getName().endsWith("."+"TestBean"){
                log.info("找到class:"+c.getCanonicalName());

            }


        }*/

    }


}
