package com.punici.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.constant.ProductConstant;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.punici.gulimall.product.dao.AttrDao;
import com.punici.gulimall.product.dao.AttrGroupDao;
import com.punici.gulimall.product.dao.CategoryDao;
import com.punici.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.punici.gulimall.product.entity.AttrEntity;
import com.punici.gulimall.product.entity.AttrGroupEntity;
import com.punici.gulimall.product.entity.CategoryEntity;
import com.punici.gulimall.product.service.AttrService;
import com.punici.gulimall.product.service.CategoryService;
import com.punici.gulimall.product.vo.AttrGroupRelationVo;
import com.punici.gulimall.product.vo.AttrRespVo;
import com.punici.gulimall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService
{
    @Autowired(required = false)
    AttrAttrgroupRelationDao relationDao;
    
    @Autowired(required = false)
    AttrGroupDao attrGroupDao;
    
    @Autowired(required = false)
    CategoryDao categoryDao;
    
    @Autowired
    CategoryService categoryService;
    
    @Override
    public PageResult queryPage(Map<String, Object> params)
    {
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), new QueryWrapper<>());
        
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
        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
        {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(attrAttrgroupRelationEntity);
        }
    }
    
    @Override
    public PageResult queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType)
    {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_type", "base".equalsIgnoreCase(attrType) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()
                : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        String key = (String) params.get("key");
        if(StringUtils.isNotBlank(key))
        {
            wrapper.eq("attr_id", key).and(c -> c.eq("", key).or().like("attr_name", key));
        }
        if(catelogId > 0)
        {
            wrapper.eq("catelog_id", catelogId);
        }
        
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageResult pageResult = new PageResult(page);
        List<AttrEntity> records = page.getRecords();
        if(!CollectionUtils.isEmpty(records))
        {
            List<AttrRespVo> vos = records.stream().map(a -> {
                AttrRespVo vo = new AttrRespVo();
                BeanUtils.copyProperties(a, vo);
                if("base".equalsIgnoreCase(attrType))
                {
                    AttrGroupEntity entity = attrGroupDao
                            .selectById(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", a.getAttrId()));
                    if(Objects.nonNull(entity))
                    {
                        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(entity.getAttrGroupId());
                        if(Objects.nonNull(attrGroupEntity))
                        {
                            vo.setGroupName(attrGroupEntity.getAttrGroupName());
                        }
                    }
                }
                
                CategoryEntity categoryEntity = categoryDao.selectById(a.getCatelogId());
                if(Objects.nonNull(categoryEntity))
                {
                    vo.setCatelogName(categoryEntity.getName());
                }
                return vo;
            }).collect(Collectors.toList());
            pageResult.setList(vos);
        }
        return pageResult;
    }
    
    @Override
    public AttrRespVo getAttrInfo(Long attrId)
    {
        AttrRespVo vo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, vo);
        // 设置分组信息
        AttrAttrgroupRelationEntity relationEntity = relationDao
                .selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if(Objects.nonNull(relationEntity))
        {
            vo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
            vo.setGroupName(Optional.ofNullable(attrGroupEntity).map(AttrGroupEntity::getAttrGroupName).orElse(""));
        }
        // 设置分类信息
        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
        {
            Long catelogId = attrEntity.getCatelogId();
            Long[] categoryPath = categoryService.findCategoryPath(catelogId);
            vo.setCatelogPath(categoryPath);
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            vo.setCatelogName(Optional.ofNullable(categoryEntity).map(CategoryEntity::getName).orElse(""));
        }
        
        return vo;
    }
    
    @Override
    @Transactional
    public void updateAttr(AttrVo attr)
    {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);
        // 设置分类信息
        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
        {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            Integer count = relationDao
                    .selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if(count > 0)
            {
                // 修改分组关联
                
                relationDao.update(relationEntity,
                        new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            }
            else
            {
                relationDao.insert(relationEntity);
            }
        }
    }
    
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId)
    {
        List<AttrAttrgroupRelationEntity> entities = relationDao
                .selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        
        List<Long> attrIds = entities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        
        if(attrIds == null || attrIds.size() == 0)
        {
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }
    
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos)
    {
        //relationDao.delete(new QueryWrapper<>().eq("attr_id",1L).eq("attr_group_id",1L));
        //
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(entities);
    }
    
    /**
     * 获取当前分组没有关联的所有属性
     * 
     * @param params
     * @param attrgroupId
     * @return
     */
    public PageResult getNoRelationAttr(Map<String, Object> params, Long attrgroupId)
    {
        // 1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        // 2、当前分组只能关联别的分组没有引用的属性
        // 2.1)、当前分类下的其他分组
        List<AttrGroupEntity> group = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> collect = group.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        
        // 2.2)、这些分组关联的属性
        List<AttrAttrgroupRelationEntity> groupId = relationDao
                .selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        List<Long> attrIds = groupId.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        
        // 2.3)、从当前分类的所有属性中移除这些属性；
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type",
                ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if(attrIds != null && attrIds.size() > 0)
        {
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key))
        {
            wrapper.and((w) -> w.eq("attr_id", key).or().like("attr_name", key));
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        return new PageResult(page);
    }
}