package com.punici.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.punici.gulimall.common.to.es.SkuEsModel;
import com.punici.gulimall.search.constant.EsConstant;
import com.punici.gulimall.search.config.GulimallElasticSearchConfig;
import com.punici.gulimall.search.service.ElasticProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElasticProductSaveServiceImpl implements ElasticProductSaveService
{
    
    @Qualifier("esRestClient")
    @Autowired(required = false)
    RestHighLevelClient client;
    
    @Override
    public boolean productSaveStatusUp(List<SkuEsModel> skuEsModels)
    {
        try
        {
            // 保存到es
            // 1.在es建立product索引，建立好映射关系
            BulkRequest bulkRequest = new BulkRequest();
            // 2.在es中保存数据

            skuEsModels.forEach(skuEsModel -> {
                // 构造保存请求
                IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
                indexRequest.id(skuEsModel.getSkuId().toString());
                indexRequest.source(JSON.toJSONString(skuEsModel), XContentType.JSON);
                bulkRequest.add(indexRequest);
            });
            // 批量保存
            BulkResponse bulk = client.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
            // TODO 错误处理
            boolean hasFailures = bulk.hasFailures();
            List<String> skuIdList = Arrays.stream(bulk.getItems()).map(BulkItemResponse::getId).collect(Collectors.toList());
            log.error("商品上架完成：{}",skuIdList);
            return hasFailures;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
