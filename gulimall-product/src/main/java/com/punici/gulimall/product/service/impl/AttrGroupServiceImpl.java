package com.punici.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.AttrGroupDao;
import com.punici.gulimall.product.entity.AttrGroupEntity;
import com.punici.gulimall.product.service.AttrGroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AttrGroupServiceImpl.class);
    
    @Override
    public PageResult queryPage(Map<String, Object> params)
    {
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>());
        
        return new PageResult(page);
    }
    
    @Override
    public PageResult queryPage(Map<String, Object> params, Long catelogId)
    {
        LOGGER.info("params:{}", JSON.toJSONString(params));
        
        if(catelogId <= 0)
        {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>());
            
            return new PageResult(page);
        }
        else
        {
            // select * from pms_attr_group where catelog_id=? and (attr_group_id=key or attr_group_name like
            // %key%)
            String key = (String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("catelog_id", catelogId);
            if(StringUtils.isNotBlank(key))
            {
                wrapper.and(w -> w.eq("attr_group_id", key).or().like("attr_group_name", key));
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            
            return new PageResult(page);
        }
    }
    
}