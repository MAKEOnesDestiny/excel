package com.zhou.demo.excel;

import com.zhou.demo.excel.annotation.valid.LengthValidator;
import com.zhou.demo.excel.bean.DynamicExcelBean;
import com.zhou.demo.excel.bean.DynamicExcelHeaders;
import com.zhou.demo.excel.bean.Header;
import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.config.ParamConst;
import com.zhou.demo.excel.config.ParameterPassHelp;
import com.zhou.demo.excel.factory.DynamicExcelFactory;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.VersionExcelFactory;
import com.zhou.demo.excel.factory.converter.TestConverter;
import com.zhou.demo.excel.factory.impl.SimpleDynamicExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import com.zhou.demo.excel.factory.impl.VersionExcelImplFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SuppressWarnings("all")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExcelApplication.class)
public class ExcelApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        File file = new File("/Users/hfzhou/Downloads/商品映射维护.xlsx");
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Workbook wb = new XSSFWorkbook(bis);

        ParameterPassHelp.setParam(ParamConst.LENGTH_LIMIT_PARAM, 40);

        ExcelFactory ef = new SimpleExcelFactory();
        List<TestBean> testBeans = null;
        try {
            testBeans = ef.toBean(wb, TestBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(testBeans);
    }

    @Test
    public void versionTest() throws IOException {
        File file = new File("/Users/hfzhou/Downloads/商品映射维护.xlsx");
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Workbook wb = new XSSFWorkbook(bis);

        ParameterPassHelp.setParam(ParamConst.LENGTH_LIMIT_PARAM, 40);

        VersionExcelFactory ef = new VersionExcelImplFactory();
        List<TestBean> testBeans = null;
        try {
            testBeans = ef.toBean(wb, TestBean.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(testBeans);
    }

    @Test
    public void dynamicTest() throws IOException {
        File file = new File("/Users/hfzhou/Downloads/商品映射维护.xlsx");
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Workbook wb = new XSSFWorkbook(bis);

        DynamicExcelFactory def = new SimpleDynamicExcelFactory();
        List<DynamicExcelBean> dynamicExcelBeans = null;
        ParameterPassHelp.setParam(ParamConst.LENGTH_LIMIT_PARAM, 2);
        try {
            Sheet sheet = wb.getSheet("商品映射维护");
            DynamicExcelHeaders headers = def.getHeadersFromExcel(sheet, 0);
            List<String> headersInStr = headers.getHeadersInStr();
            List<Header> headerList = headers.getHeaders();
            headerList.get(0).setValidators(LengthValidator.class);
            headerList.get(0).setConverter(TestConverter.class);
            //
            List<Cell> cells = headers.getHeadersInCell();
            Map m1 = headers.getCellHeadersAsMap();
            Map m2 = headers.getStrHeadersAsMap();
            //
            dynamicExcelBeans = def.toDynamicBean(sheet, headers);
            System.out.println(dynamicExcelBeans);
            dynamicExcelBeans.get(0).getContentByHeader(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
