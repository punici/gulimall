package com.punici.gulimall.product.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.punici.gulimall.product.entity.SkuInfoEntity;

/**
 * sku信息
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity>
{

}
