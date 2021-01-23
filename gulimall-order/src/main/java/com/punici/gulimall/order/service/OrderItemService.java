package com.punici.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.order.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:30:29
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageResult queryPage(Map<String, Object> params);
}

