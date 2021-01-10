package com.punici.gulimall.order.dao;

import com.punici.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:30:29
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
