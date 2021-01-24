package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.product.entity.AttrEntity;
import com.punici.gulimall.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface AttrService extends IService<AttrEntity> {

    PageResult queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageResult queryBaseAttrPage(Map<String, Object> params, Long catelogId);
}

