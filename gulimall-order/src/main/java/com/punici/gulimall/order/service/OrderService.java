package com.punici.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:30:29
 */
public interface OrderService extends IService<OrderEntity> {

    PageResult queryPage(Map<String, Object> params);
}

