package com.zhou.demo.excel;

import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.config.ParamConst;
import com.zhou.demo.excel.config.ParameterPassHelp;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import com.zhou.demo.excel.utils.ExceptionUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(testBeans);
    }

}
