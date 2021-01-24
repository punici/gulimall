package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.product.entity.BrandEntity;
import com.punici.gulimall.product.entity.CategoryBrandRelationEntity;
import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity>
{
    
    PageUtils queryPage(Map<String, Object> params);
    
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);
    
    void updateBrand(Long brandId, String name);
    
    void updateCategory(Long catId, String name);
    
    List<BrandEntity> getBrandsByCatId(Long catId);
    
}
