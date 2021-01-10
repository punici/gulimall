package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utills.PageUtils;
import com.punici.gulimall.product.entity.SkuInfoEntity;
import java.util.Map;

/**
 * sku信息
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 14:46:53
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

