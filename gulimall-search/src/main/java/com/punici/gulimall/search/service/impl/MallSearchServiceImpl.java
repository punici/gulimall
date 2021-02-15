package com.punici.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.punici.gulimall.common.to.es.SkuEsModel;
import com.punici.gulimall.search.config.GulimallElasticSearchConfig;
import com.punici.gulimall.search.constant.EsConstant;
import com.punici.gulimall.search.service.MallSearchService;
import com.punici.gulimall.search.vo.SearchParam;
import com.punici.gulimall.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class MallSearchServiceImpl implements MallSearchService
{
    @Qualifier("esRestClient")
    @Autowired(required = false)
    private RestHighLevelClient client;
    
    /**
     *
     * @param param 检索的所有参数
     * @return Object 检索返回的结果
     */
    @Override
    public SearchResult search(SearchParam param)
    {
        SearchResult result = null;
        // 动态构建出查询需要的dsl语句
        try
        {
            // 准备检索请求
            
            SearchRequest request = buildSearchRequest(param);
            SearchResponse response = client.search(request, GulimallElasticSearchConfig.COMMON_OPTIONS);
            result = buildSearchResult(response, param);
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 准备检索请求 #模糊匹配 过滤(按照属性，分类，品牌，价格区间，库存) 排序 分页，高亮，聚合分析
     *
     * @return SearchRequest
     * @param param 查询参数
     */
    private SearchRequest buildSearchRequest(SearchParam param)
    {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();// 构建DSL语句
        /*
         * 模糊匹配 过滤(按照属性，分类，品牌，价格区间，库存)
         */
        // 1、构建bool-query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 1.1 must 模糊匹配
        if(StringUtils.isNotBlank(param.getKeyWord()))
        {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyWord()));
        }
        // 1.2 filter 按照3级分类id查询
        if(Objects.nonNull(param.getCatalog3Id()))
        {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        // 1.2 filter 按照品牌id查询
        if(!CollectionUtils.isEmpty(param.getBrandId()))
        {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        // 1.3 filter 按照是否有库存d查询
        boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        // 1.4 filter 按照价格区间查询
        if(StringUtils.isNotBlank(param.getSkuPrice()))
        {
            // skuPrice=1_500/_500/1_
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if(s.length == 2)
            {
                rangeQuery.gte(s[0]);
                rangeQuery.lte(s[1]);
            }
            else if(s.length == 1)
            {
                if(param.getSkuPrice().startsWith("_"))
                {
                    rangeQuery.lte(s[0]);
                }
                if(param.getSkuPrice().endsWith("_"))
                {
                    rangeQuery.gte(s[0]);
                }
                
            }
            
            boolQuery.filter(rangeQuery);
        }
        // 1.5 filter 按照属性查询
        if(!CollectionUtils.isEmpty(param.getAttrs()))
        {
            // attrs=1_5寸:8寸&attrs=2_16G:8G
            for(String attr : param.getAttrs())
            {
                BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                String[] s = attr.split("_");
                // 属性id
                String attrId = s[0];
                // 属性值
                String[] attrValues = s[1].split(":");
                nestedBoolQueryBuilder.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQueryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                // 每一个属性查询条件都得都得生成一个nestedQuery
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", nestedBoolQueryBuilder,
                        ScoreMode.None);
                boolQuery.filter(nestedQueryBuilder);
            }
        }
        sourceBuilder.query(boolQuery);
        
        /*
         * 排序 分页，高亮，
         */
        // 2.1 排序
        String sortStr = param.getSort();
        if(StringUtils.isNotBlank(sortStr))
        {
            // sort=hotScore_asc/desc
            String[] sort = sortStr.split("_");
            
            sourceBuilder.sort(sort[0], sort[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
            sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE);
            sourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE);
            // 高亮
            if(StringUtils.isNotBlank(param.getKeyWord()))
            {
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                highlightBuilder.field("skuTitle");
                highlightBuilder.preTags("<b style='color:red'>");
                highlightBuilder.postTags("</b>");
                sourceBuilder.highlighter(highlightBuilder);
            }
        }
        /*
         * 聚合分析
         */
        // 1 品牌聚合
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg");
        sourceBuilder.aggregation(brandAgg);
        brandAgg.field("brandId").size(50);
        // 品牌聚合的字聚合
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("branName").size(1));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        // 5.2 按照catalog聚合
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg").field("catalogId");
        TermsAggregationBuilder catalogNameAgg = AggregationBuilders.terms("catalog_name_agg").field("catalogName");
        catalogAgg.subAggregation(catalogNameAgg);
        sourceBuilder.aggregation(catalogAgg);
        
        // 5.3 按照attrs聚合
        NestedAggregationBuilder nestedAggregationBuilder = new NestedAggregationBuilder("attrs", "attrs");
        // 按照attrId聚合
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        // 按照attrId聚合之后再按照attrName和attrValue聚合
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attr_name_agg").field("attrs.attrName");
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue");
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        
        nestedAggregationBuilder.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(nestedAggregationBuilder);
        
        log.debug("构建的DSL语句 {}", sourceBuilder.toString());
        
        return new SearchRequest(new String[] { EsConstant.PRODUCT_INDEX }, sourceBuilder);
    }
    
    /**
     * 构建结果数据
     *
     * @param response
     * @param param
     * @return SearchResult
     */
    private SearchResult buildSearchResult(SearchResponse response, SearchParam param)
    {
        SearchResult result = new SearchResult();
        SearchHits hits = response.getHits();
        List<SkuEsModel> skuEsModelList = new ArrayList<>();
        if(Objects.nonNull(hits.getHits()) && hits.getHits().length > 0)
        {
            for(SearchHit hit : hits.getHits())
            {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if(StringUtils.isNotBlank(param.getKeyWord()))
                {
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    HighlightField highlightField = highlightFields.get("skuTitle");
                    String skuTitle = highlightField.getFragments()[0].string();
                    skuEsModel.setSkuTitle(skuTitle);
                }
                skuEsModelList.add(skuEsModel);
            }
        }
        // 商品
        result.setProduct(skuEsModelList);
        // 属性
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        // ParsedNested用于接收内置属性的聚合
        ParsedNested parsedNested = response.getAggregations().get("attrs");
        ParsedLongTerms attrIdAgg = parsedNested.getAggregations().get("attr_id_agg");
        for(Terms.Bucket bucket : attrIdAgg.getBuckets())
        {
            // 5.1 查询属性id
            Long attrId = bucket.getKeyAsNumber().longValue();
            
            Aggregations subAttrAgg = bucket.getAggregations();
            // 5.2 查询属性名
            ParsedStringTerms attrNameAgg = subAttrAgg.get("attr_name_agg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            // 5.3 查询属性值
            ParsedStringTerms attrValueAgg = subAttrAgg.get("attr_value_agg");
            List<String> attrValues = new ArrayList<>();
            for(Terms.Bucket attrValueAggBucket : attrValueAgg.getBuckets())
            {
                String attrValue = attrValueAggBucket.getKeyAsString();
                attrValues.add(attrValue);
                List<SearchResult.NavVo> navVos = new ArrayList<>();
            }
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo(attrId, attrName, attrValues);
            attrVos.add(attrVo);
        }
        result.setAttrs(attrVos);
        // 品牌
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        Aggregations aggregations = response.getAggregations();
        // ParsedLongTerms用于接收terms聚合的结果，并且可以把key转化为Long类型的数据
        ParsedLongTerms brandAgg = aggregations.get("brandAgg");
        for(Terms.Bucket bucket : brandAgg.getBuckets())
        {
            // 3.1 得到品牌id
            Long brandId = bucket.getKeyAsNumber().longValue();
            
            Aggregations subBrandAggs = bucket.getAggregations();
            // 3.2 得到品牌图片
            ParsedStringTerms brandImgAgg = subBrandAggs.get("brandImgAgg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            // 3.3 得到品牌名字
            Terms brandNameAgg = subBrandAggs.get("brandNameAgg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo(brandId, brandName, brandImg);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
        // 分类
        // ParsedLongTerms用于接收terms聚合的结果，并且可以把key转化为Long类型的数据
        ParsedLongTerms catalogAgg = response.getAggregations().get("catalog_agg");
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalogAgg.getBuckets();
        for(Terms.Bucket bucket : buckets)
        {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            String keyAsString = bucket.getKeyAsString();
            // 获得分类id
            catalogVo.setCatalogId(Long.parseLong(keyAsString));
            // 得到分类名
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            List<? extends Terms.Bucket> catalogNameAggBuckets = catalogNameAgg.getBuckets();
            if(!CollectionUtils.isEmpty(catalogNameAggBuckets))
            {
                String catalogName = catalogNameAggBuckets.get(0).getKeyAsString();
                catalogVo.setCatalogName(catalogName);
            }
        }
        result.setCatalogs(catalogVos);
        // 页码
        result.setPageNum(param.getPageNum());
        // 总记录
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        // 总页码
        result.setTotalPages((total % EsConstant.PRODUCT_PAGE_SIZE) == 0 ? (int) (total / EsConstant.PRODUCT_PAGE_SIZE)
                : (int) (total / EsConstant.PRODUCT_PAGE_SIZE + 1));
        return result;
    }
    
}
