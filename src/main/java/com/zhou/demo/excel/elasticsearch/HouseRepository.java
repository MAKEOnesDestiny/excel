package com.zhou.demo.excel.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface HouseRepository extends ElasticsearchRepository<HouseIndexTemplate, Long> {


}
