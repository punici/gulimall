package com.punici.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.BrandDao;
import com.punici.gulimall.product.dao.CategoryBrandRelationDao;
import com.punici.gulimall.product.dao.CategoryDao;
import com.punici.gulimall.product.entity.BrandEntity;
import com.punici.gulimall.product.entity.CategoryBrandRelationEntity;
import com.punici.gulimall.product.entity.CategoryEntity;
import com.punici.gulimall.product.service.BrandService;
import com.punici.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity>
        implements CategoryBrandRelationService
{
    
    @Autowired(required = false)
    BrandDao brandDao;
    
    @Autowired(required = false)
    CategoryDao categoryDao;
    
    @Autowired(required = false)
    CategoryBrandRelationDao relationDao;
    
    @Autowired
    BrandService brandService;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<CategoryBrandRelationEntity> page = this.page(new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>());
        return new PageUtils(page);
    }
    
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation)
    {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        // 1、查询详细名字
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
        
    }
    
    @Override
    public void updateBrand(Long brandId, String name)
    {
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(name);
        this.update(relationEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
    }
    
    @Override
    public void updateCategory(Long catId, String name)
    {
        this.baseMapper.updateCategory(catId, name);
    }
    
    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId)
    {
        List<CategoryBrandRelationEntity> catelogId = relationDao
                .selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        return catelogId.stream().map(item -> {
            Long brandId = item.getBrandId();
            return brandService.getById(brandId);
        }).collect(Collectors.toList());
    }
}