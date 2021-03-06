package com.punici.gulimall.search.vo;

import java.util.List;

/**
 * 封装页面传过来的所有查询条件
 */
public class SearchParam
{
    private String keyWord;// 全文匹配
    
    // 品牌id,可以多选
    private List<Long> brandId;
    
    private Long catalog3Id;// 3级分类id
    
    /**
     * sort=saleCount_asc/desc sort=skuPrice_asc/desc sort=hotScore_asc/desc
     */
    private String Sort;// 排序条件
    
    // 是否显示有货
    private Integer hasStock = 1;
    
    // 价格区间查询
    private String skuPrice;
    
    // 按照属性进行筛选
    private List<String> attrs;
    
    // 页码
    private Integer pageNum;
    
    // 原生的所有查询条件
    private String _queryString;
    
    public SearchParam()
    {
    }
    
    public String getKeyWord()
    {
        return keyWord;
    }
    
    public void setKeyWord(String keyWord)
    {
        this.keyWord = keyWord;
    }
    
    public List<Long> getBrandId()
    {
        return brandId;
    }
    
    public void setBrandId(List<Long> brandId)
    {
        this.brandId = brandId;
    }
    
    public Long getCatalog3Id()
    {
        return catalog3Id;
    }
    
    public void setCatalog3Id(Long catalog3Id)
    {
        this.catalog3Id = catalog3Id;
    }
    
    public String getSort()
    {
        return Sort;
    }
    
    public void setSort(String sort)
    {
        Sort = sort;
    }
    
    public Integer getHasStock()
    {
        return hasStock;
    }
    
    public void setHasStock(Integer hasStock)
    {
        this.hasStock = hasStock;
    }
    
    public String getSkuPrice()
    {
        return skuPrice;
    }
    
    public void setSkuPrice(String skuPrice)
    {
        this.skuPrice = skuPrice;
    }
    
    public List<String> getAttrs()
    {
        return attrs;
    }
    
    public void setAttrs(List<String> attrs)
    {
        this.attrs = attrs;
    }
    
    public Integer getPageNum()
    {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }
    
    public String get_queryString()
    {
        return _queryString;
    }
    
    public void set_queryString(String _queryString)
    {
        this._queryString = _queryString;
    }
}
