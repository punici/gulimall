package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageResult queryPage(Map<String, Object> params);
}

