package com.punici.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:06:20
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageResult queryPage(Map<String, Object> params);
}

