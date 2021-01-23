package com.punici.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.coupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:06:19
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageResult queryPage(Map<String, Object> params);
}

