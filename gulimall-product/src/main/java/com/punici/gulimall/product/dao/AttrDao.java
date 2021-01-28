package com.punici.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.punici.gulimall.product.entity.AttrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 商品属性
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity>
{
    List<Long> selectSearchAttrIds(@Param("attrIds") List<Long> attrIds);
}
