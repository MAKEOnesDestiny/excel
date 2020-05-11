package com.zhou.demo.excel.controller;


import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.VersionExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import com.zhou.demo.excel.factory.impl.VersionExcelImplFactory;
import com.zhou.demo.excel.utils.TokenUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/test/controller")
@Log4j2
public class TestController {

    @RequestMapping("/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request
            , @RequestParam(value = "preview", required = false, defaultValue = "false") boolean preview
            , @RequestPart(value = "file") Part part) throws Exception {
        ExcelFactory factory = new SimpleExcelFactory() {
            @Override
            public boolean skipBlank() {
                return false;
            }
        };
        /*VersionExcelFactory factory = new VersionExcelImplFactory() {
            @Override
            public boolean skipBlank() {
                return true;
            }
        };*/
        List list = null;
        try {
            InputStream is = part.getInputStream();
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            list = factory.toBean(bis, TestBean.class);
            bis.reset();
            list = factory.toBean(bis, TestBean.class);
        } catch (ExcelDataWrongException e) {
            e.printStackTrace();
            return e.toString();
        }
        if (preview) {
            String uuid = TokenUtil.getUUID();
            request.getSession().setAttribute(uuid, list);
        }
        return list;
    }

    @RequestMapping("/export")
    public void export(@RequestParam("bean") String bean, HttpServletResponse response) throws Exception {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        String classInStr = "com.zhou.demo.excel.bean." + bean;
        Class clazz = Class.forName(classInStr);
        if (clazz == null) throw new RuntimeException("错误的参数:" + bean);
        ExcelFactory factory = new SimpleExcelFactory();
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=file.xls");
        OutputStream os = response.getOutputStream();
    }

    @RequestMapping("/preview")
    @ResponseBody
    public Object preview(@RequestParam("preCode") String preCode, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(preCode)) {
            return null;
        }
        HttpSession session = request.getSession();
        Object result = session.getAttribute(preCode);
        if (result == null) {
            //没有找到preCode
        }
        return result;
    }

/*
    @RequestMapping("/session")
    @ResponseBody
    public String session(HttpServletRequest request) throws Exception {
        return request.getSession().getId();
    }
*/


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
