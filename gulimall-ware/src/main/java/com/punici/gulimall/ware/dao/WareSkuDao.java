package com.punici.gulimall.ware.dao;

import com.punici.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity>
{
    
    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);
}
