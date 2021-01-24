package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.product.entity.AttrGroupEntity;
import com.punici.gulimall.product.vo.AttrGroupWithAttrsVo;
import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface AttrGroupService extends IService<AttrGroupEntity>
{
    
    PageUtils queryPage(Map<String, Object> params);
    
    PageUtils queryPage(Map<String, Object> params, Long catelogId);
    
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
    
}
