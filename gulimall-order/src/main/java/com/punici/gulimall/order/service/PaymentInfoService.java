package com.punici.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.order.entity.PaymentInfoEntity;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:30:29
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageResult queryPage(Map<String, Object> params);
}

