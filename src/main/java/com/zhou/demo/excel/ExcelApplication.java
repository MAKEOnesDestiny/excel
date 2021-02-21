package com.zhou.demo.excel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {
          ElasticsearchAutoConfiguration.class
        , ElasticsearchDataAutoConfiguration.class
        , ElasticsearchRepositoriesAutoConfiguration.class})
public class ExcelApplication {

    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(ExcelApplication.class, args);
        System.out.println();
    }

}
