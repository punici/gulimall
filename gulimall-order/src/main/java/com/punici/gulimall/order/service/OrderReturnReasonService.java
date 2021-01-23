package com.punici.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.order.entity.OrderReturnReasonEntity;

import java.util.Map;

/**
 * 退货原因
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:30:29
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageResult queryPage(Map<String, Object> params);
}

