package com.punici.gulimall.product.dao;

import com.punici.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
