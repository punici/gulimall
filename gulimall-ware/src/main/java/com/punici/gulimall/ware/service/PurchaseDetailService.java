package com.punici.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageResult queryPage(Map<String, Object> params);
}

