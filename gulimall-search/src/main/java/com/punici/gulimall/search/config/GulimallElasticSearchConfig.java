package com.punici.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1. 导入依赖 2. 编写配置,给容器中注入RestHighLevelClient 3. 参照官方API
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html
 */
@Configuration
public class GulimallElasticSearchConfig
{
    
    public static final RequestOptions COMMON_OPTIONS;
    static
    {
        // 为每一个请求配置
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // builder.addHeader("Authorization", "Bearer " + TOKEN);
        // builder.setHttpAsyncResponseConsumerFactory(
        // new HttpAsyncResponseConsumerFactory
        // .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }
    
    @Bean
    public RestHighLevelClient esRestClient()
    {
        // RestClientBuilder builder=null;
        // RestClient.builder(new HttpHost("192.168.10.10", 9200, "http"));
        // RestHighLevelClient client=new RestHighLevelClient(builder);
        return new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.10.10", 9200, "http")));
        
    }
}
