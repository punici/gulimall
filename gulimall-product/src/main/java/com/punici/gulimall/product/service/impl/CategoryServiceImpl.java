package com.punici.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.CategoryDao;
import com.punici.gulimall.product.entity.CategoryEntity;
import com.punici.gulimall.product.service.CategoryBrandRelationService;
import com.punici.gulimall.product.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageResult queryPage(Map<String, Object> params)
    {
        IPage<CategoryEntity> page = this.page(new Query<CategoryEntity>().getPage(params), new QueryWrapper<CategoryEntity>());
        
        return new PageResult(page);
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
    public void removeMenuByIds(Long[] catIds)
    {
        // TODO 1、检查当前删除的菜单，是否被别的地方应用
        baseMapper.deleteBatchIds(Arrays.asList(catIds.clone()));
    }
    
    @Override
    public Long[] findCategoryPath(Long catelogId)
    {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[0]);
    }

    /**
     * 级联更新
     * @param category
     */
    @Override
    public void updateCascad(CategoryEntity category)
    {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category);
    }
    
    private List<Long> findParentPath(Long catelogId, List<Long> paths)
    {
        // 收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid() != 0)
        {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }
    
}