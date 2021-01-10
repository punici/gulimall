package com.punici.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.punici.gulimall.product.entity.SpuCommentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 14:46:52
 */
@Mapper
public interface SpuCommentDao extends BaseMapper<SpuCommentEntity> {
	
}
