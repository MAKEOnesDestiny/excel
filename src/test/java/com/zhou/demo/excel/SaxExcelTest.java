package com.zhou.demo.excel;

import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleSaxExcelFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

// web环境配置
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SaxExcelTest.class)
public class SaxExcelTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testSaxFactory() throws Exception {
        MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        InputStream is = new FileInputStream("/Users/hfzhou/Desktop/商品映射维护.xlsx");

        ExcelFactory saxEF = new SimpleSaxExcelFactory();
        List<TestBean> beans = saxEF.toBean(is, TestBean.class);
        System.out.println();
    }


}
