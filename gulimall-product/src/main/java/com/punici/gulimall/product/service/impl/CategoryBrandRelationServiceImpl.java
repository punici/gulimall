package com.punici.gulimall.product.service.impl;

        import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
        import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
        import com.baomidou.mybatisplus.core.metadata.IPage;
        import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
        import com.punici.gulimall.common.utils.PageResult;
        import com.punici.gulimall.common.utils.Query;
        import com.punici.gulimall.product.dao.BrandDao;
        import com.punici.gulimall.product.dao.CategoryBrandRelationDao;
        import com.punici.gulimall.product.dao.CategoryDao;
        import com.punici.gulimall.product.entity.BrandEntity;
        import com.punici.gulimall.product.entity.CategoryBrandRelationEntity;
        import com.punici.gulimall.product.entity.CategoryEntity;
        import com.punici.gulimall.product.service.CategoryBrandRelationService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.Map;
        import java.util.Objects;

@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity>
        implements CategoryBrandRelationService
{
    @Autowired(required = false)
    BrandDao brandDao;

    @Autowired(required = false)
    CategoryDao categoryDao;

    @Override
    public PageResult queryPage(Map<String, Object> params)
    {
        IPage<CategoryBrandRelationEntity> page = this.page(new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>());

        return new PageResult(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation)
    {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        // 查询
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if(Objects.isNull(brandEntity) || Objects.isNull(categoryEntity))
        {
            return;
        }
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(BrandEntity brand)
    {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brand.getBrandId());
        categoryBrandRelationEntity.setBrandName(brand.getName());
        this.update(categoryBrandRelationEntity,
                new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brand.getBrandId()));
    }

    @Override
    public void updateCategory(CategoryEntity category)
    {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogName(category.getName());
        categoryBrandRelationEntity.setCatelogId(category.getCatId());
        this.update(categoryBrandRelationEntity,
                new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", category.getCatId()));
    }
}