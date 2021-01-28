package com.punici.gulimall.search.test;

import com.alibaba.fastjson.JSON;
import com.punici.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallSearchApplicationTest
{
    
    // @Qualifier("elasticsearchRestHighLevelClient")
    @Autowired(required = false)
    private RestHighLevelClient client;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Test
    void contextLoads()
    {
        assertNotNull(client);
        System.out.println(client);
    }
    
    /**
     * 存储数据到es
     */
    @Test
    void indexData() throws IOException
    {
        assertNotNull(client);
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        // indexRequest.source("user", "kimchy", "postDate", new Date(), "message", "trying out
        // Elasticsearch");
        User user = new User();
        user.setUserName("123");
        user.setGender("123");
        user.setAge(10);
        
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);// 要保存的内容
        // 执行操作
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        // 提取有用的响应数据
        assertNotNull(index);
        logger.info(JSON.toJSONString(index));
        
    }
    
    @Test
    void searchData() throws IOException
    {
        assertNotNull(client);
        // 1. 创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        
        // 1.1）指定索引
        searchRequest.indices("bank");
        // 1.2）构造检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("address", "Mill"));
        // sourceBuilder.from();
        // sourceBuilder.size();
        
        // 1.2.1)按照年龄分布进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);
        
        // 1.2.2)计算平均年龄
        AvgAggregationBuilder ageAvg = AggregationBuilders.avg("ageAvg").field("age");
        sourceBuilder.aggregation(ageAvg);
        // 1.2.3)计算平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);
        
        System.out.println("检索条件：" + sourceBuilder);
        searchRequest.source(sourceBuilder);
        // 2. 执行检索
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("检索结果：" + searchResponse);
        // 3.分析结果
        logger.info("searchResponse:{}", JSON.toJSONString(searchResponse));
        
        // 3.1 获取查到的所有数据
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits)
        {
            // hit.getIndex();
            // hit.getPrimaryTerm();
            // hit.getId();
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println(account);
        }
        // 3.2获取这次解锁到的分析信息
        Aggregations aggregations = searchResponse.getAggregations();
//        List<Aggregation> aggregationList = aggregations.asList();
//        for (Aggregation aggregation : aggregationList) {
//            String aggregationName = aggregation.getName();
//            System.out.println("当前聚合的名字："+aggregationName);
//
//        }
        Terms ageAggAggregation = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAggAggregation.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄："+keyAsString+"==>"+bucket.getDocCount());
        }
        Avg balanceAvgAgg = aggregations.get("balanceAvg");
        System.out.println("平均薪资"+balanceAvgAgg.getValueAsString());
    }
    
    @Data
    static class User
    {
        private String userName;
        
        private String gender;
        
        private Integer age;
    }
    
    @ToString
    @Data
    static class Account
    {
        
        private int account_number;
        
        private int balance;
        
        private String firstname;
        
        private String lastname;
        
        private int age;
        
        private String gender;
        
        private String address;
        
        private String employer;
        
        private String email;
        
        private String city;
        
        private String state;
        
    }
}