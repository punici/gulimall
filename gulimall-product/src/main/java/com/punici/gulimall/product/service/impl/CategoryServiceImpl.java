package com.punici.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.CategoryDao;
import com.punici.gulimall.product.entity.CategoryEntity;
import com.punici.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService
{
    
    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<CategoryEntity> page = this.page(new Query<CategoryEntity>().getPage(params), new QueryWrapper<CategoryEntity>());
        
        return new PageUtils(page);
    }
    
    @Override
    public List<CategoryEntity> listWithTree()
    {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        if(CollectionUtils.isEmpty(entities))
        {
            return entities;
        }
        return entities.stream().filter(o -> o.getParentCid().equals(0L)).peek(c -> c.setChildren(getChildren(c, entities)))
                .sorted(Comparator.comparingInt(c -> ((c.getSort() == null) ? 0 : c.getSort()))).collect(Collectors.toList());
    }
    private List<CategoryEntity> getChildren(CategoryEntity entity, List<CategoryEntity> entities)
    {
        return entities.stream().filter(e -> e.getParentCid().equals(entity.getCatId()))
                .peek(c -> c.setChildren(getChildren(c, entities)))
                .sorted(Comparator.comparingInt(c -> ((c.getSort() == null) ? 0 : c.getSort()))).collect(Collectors.toList());
    }


    @Override
    public void removeMenuByIds(Long[] catIds) {
        // TODO 1、检查当前删除的菜单，是否被别的地方应用
        baseMapper.deleteBatchIds(Arrays.asList(catIds.clone()));
    }
}