package com.punici.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.BrandDao;
import com.punici.gulimall.product.entity.BrandEntity;
import com.punici.gulimall.product.service.BrandService;
import com.punici.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService
{
    @Autowired(required = false)
    CategoryBrandRelationService categoryBrandRelationService;
    
    @Override
    public PageResult queryPage(Map<String, Object> params)
    {
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(key))
        {
            wrapper.eq("brand_id", key).or().like("name", key);
        }
        
        IPage<BrandEntity> page = this.page(new Query<BrandEntity>().getPage(params), wrapper);
        
        return new PageResult(page);
    }
    
    @Override
    public void updateDetail(BrandEntity brand)
    {
        // 保证冗余字段的数据一致
        this.updateById(brand);
        if(StringUtils.isNotBlank(brand.getName()))
        {
            categoryBrandRelationService.updateBrand(brand);

            //TODO 更新其他关联
        }
    }
    
}