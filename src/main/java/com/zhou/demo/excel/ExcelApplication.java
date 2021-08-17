package com.zhou.demo.excel;

import com.zhou.demo.excel.bean.BigData;
import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleSaxExcelFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication()
public class ExcelApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext ac = SpringApplication.run(ExcelApplication.class, args);
        Thread.sleep(5000L);
        System.out.println();

        System.gc();
        InputStream is = new FileInputStream("/Users/hfzhou/Desktop/excel内存测试/test-07.xlsx");

        System.out.println("start ==> "+ new Date());
        ExcelFactory saxEF = new SimpleSaxExcelFactory();
//        ExcelFactory saxEF = new SimpleExcelFactory();
        List<BigData> beans = saxEF.toBean(is, BigData.class); //20s完成
        System.out.println("end ==> " + new Date());
        while(true){
            Thread.sleep(100000L);
        }
    }

}
