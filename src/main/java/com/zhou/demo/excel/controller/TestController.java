package com.zhou.demo.excel.controller;


import com.zhou.demo.excel.annotation.valid.NotBlankValidator;
import com.zhou.demo.excel.annotation.valid.TimeScopeValidator;
import com.zhou.demo.excel.bean.DynamicExcelBean;
import com.zhou.demo.excel.bean.DynamicExcelHeaders;
import com.zhou.demo.excel.bean.Header;
import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.config.ParamConst;
import com.zhou.demo.excel.config.ParameterPassHelp;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.DynamicExcelFactory;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleDynamicExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import com.zhou.demo.excel.spring.ExcelToBean;
import com.zhou.demo.excel.utils.TokenUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/controller")
@Log4j2
public class TestController {

    public TestController() {
        System.out.println("我是testController");
    }

    @RequestMapping("/testParamPass")
    @ResponseBody
    public Object testParamPass(HttpServletRequest request
            , @RequestParam(value = "preview", required = false, defaultValue = "false") boolean preview
            , @RequestPart(value = "file") Part part) throws Exception {
        InputStream is = part.getInputStream();
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Workbook wb = new XSSFWorkbook(bis);

        ParameterPassHelp.setParam(ParamConst.LENGTH_LIMIT_PARAM, 5);

        ExcelFactory ef = new SimpleExcelFactory();
        List<TestBean> testBeans;
        try {
            testBeans = ef.toBean(wb, TestBean.class);
        } catch (Exception e) {
            return e.toString();
        }
        return testBeans;
    }


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
        DynamicExcelFactory dFactory = new SimpleDynamicExcelFactory();
        List list = null;
        try {
            InputStream is = part.getInputStream();
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            list = factory.toBean(bis, TestBean.class);

            bis.reset();

            Workbook wb = new XSSFWorkbook(bis);
            Sheet sheet = wb.getSheet("值班表");

            DynamicExcelHeaders headers = dFactory.getHeadersFromExcel(sheet, 0);
            List<Header> headerList = headers.getHeaders();
            for (Header h : headerList) {
                if ("时间".equals(h.getHeaderInStr())) {
                    h.setValidators(NotBlankValidator.class, TimeScopeValidator.class);
                    continue;
                }
                if ("备注".equals(h.getHeaderInStr())) {
                    continue;
                }
                //                h.setValidators(NotBlankValidator.class);
            }
            List<DynamicExcelBean> toBean = dFactory.toDynamicBean(sheet, headers);
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
        if (clazz == null) {
            throw new RuntimeException("错误的参数:" + bean);
        }
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


    @RequestMapping("/upload2")
    @ResponseBody
    public Object upload2(@ExcelToBean(targetClass = TestBean.class, file = "file") List<TestBean> list)
            throws Exception {
        System.out.println(111);
        System.gc();
        return list;
    }

}
