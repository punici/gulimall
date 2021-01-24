package com.punici.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.ware.entity.WareSkuEntity;
import java.util.Map;

/**
 * 商品库存
 *
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
public interface WareSkuService extends IService<WareSkuEntity>
{
    PageUtils queryPage(Map<String, Object> params);
    
    void addStock(Long skuId, Long wareId, Integer skuNum);
}