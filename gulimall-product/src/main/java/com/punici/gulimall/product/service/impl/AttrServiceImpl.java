package com.punici.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.punici.gulimall.product.dao.AttrDao;
import com.punici.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.punici.gulimall.product.entity.AttrEntity;
import com.punici.gulimall.product.service.AttrService;
import com.punici.gulimall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService
{
    @Autowired(required = false)
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    
    @Override
    public PageResult queryPage(Map<String, Object> params)
    {
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), new QueryWrapper<AttrEntity>());
        
        return new PageResult(page);
    }
    
    @Override
    @Transactional
    public void saveAttr(AttrVo attr)
    {
        if(Objects.isNull(attr))
        {
            return;
        }
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // 保存基本数据
        this.save(attrEntity);
        // 保存关联关系
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
    }
    
    @Override
    public PageResult queryBaseAttrPage(Map<String, Object> params, Long catelogId)
    {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(StringUtils.isNotBlank(key))
        {
            wrapper.eq("attr_id", key).and(c -> c.eq("", key).or().like("attr_name", key));
        }
        if (catelogId>0){
            wrapper.eq("catelog_id",catelogId);
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        return new PageResult(page);
    }
    
}