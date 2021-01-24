package com.punici.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.SpuInfoDescDao;
import com.punici.gulimall.product.entity.SpuInfoDescEntity;
import com.punici.gulimall.product.service.SpuInfoDescService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService
{
    
    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<SpuInfoDescEntity> page = this.page(new Query<SpuInfoDescEntity>().getPage(params), new QueryWrapper<>());
        return new PageUtils(page);
    }
    
    @Override
    public void saveSpuInfoDesc(SpuInfoDescEntity descEntity)
    {
        this.baseMapper.insert(descEntity);
    }
}