package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageResult queryPage(Map<String, Object> params);

    PageResult queryPage(Map<String, Object> params, Long catelogId);
}

