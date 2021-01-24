package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.punici.gulimall.product.vo.AttrGroupRelationVo;
import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity>
{
    
    PageUtils queryPage(Map<String, Object> params);
    
    void saveBatch(List<AttrGroupRelationVo> vos);
    
}
