package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.product.entity.CategoryEntity;
import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface CategoryService extends IService<CategoryEntity>
{
    
    PageUtils queryPage(Map<String, Object> params);
    
    List<CategoryEntity> listWithTree();
    
    void removeMenuByIds(List<Long> asList);
    
    /**
     * 找到catelogId的完整路径； [父/子/孙]
     * 
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);
    
    void updateCascade(CategoryEntity category);
    
}
