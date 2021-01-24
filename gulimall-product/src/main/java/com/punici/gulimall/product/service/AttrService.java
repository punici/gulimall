package com.punici.gulimall.product.service;

import com.punici.gulimall.product.vo.AttrGroupRelationVo;
import com.punici.gulimall.product.vo.AttrRespVo;
import com.punici.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.product.entity.AttrEntity;
import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface AttrService extends IService<AttrEntity>
{
    
    PageUtils queryPage(Map<String, Object> params);
    
    void saveAttr(AttrVo attr);
    
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);
    
    AttrRespVo getAttrInfo(Long attrId);
    
    void updateAttr(AttrVo attr);
    
    List<AttrEntity> getRelationAttr(Long attrgroupId);
    
    void deleteRelation(AttrGroupRelationVo[] vos);
    
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
    
}
