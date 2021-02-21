package com.zhou.demo.elasticsearch;

import com.zhou.demo.excel.ExcelApplication;
import com.zhou.demo.excel.elasticsearch.HouseIndexTemplate;
import com.zhou.demo.excel.elasticsearch.HouseRepository;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExcelApplication.class)
public class ESTest {

    @Resource
    private HouseRepository houseRepository;

    @Test
    public void testES(){
        HouseIndexTemplate template = new HouseIndexTemplate();
        template.setId(1L);
        template.setName("hfzhou");
        template.setPrice(10);
        template.setTitle("我的标题");
        houseRepository.save(template);
        System.out.println(111);
    }

}
