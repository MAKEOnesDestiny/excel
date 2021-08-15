package com.zhou.demo.excel;

import com.zhou.demo.excel.bean.TestBean;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.impl.SimpleSaxExcelFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
        System.out.println();
    }

}
