package com.punici.gulimall.coupon.dao;

import com.punici.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:06:20
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
