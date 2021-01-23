package com.punici.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.ware.entity.PurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageResult queryPage(Map<String, Object> params);
}

