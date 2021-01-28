package com.punici.gulimall.product.dao;

import com.punici.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity>
{
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
