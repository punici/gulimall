package com.punici.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;
import com.punici.gulimall.product.entity.CategoryBrandRelationEntity;
import com.punici.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController
{
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:categorybrandrelation:list")
    public Result list(@RequestParam Map<String, Object> params)
    {
        PageResult page = categoryBrandRelationService.queryPage(params);
        
        return Result.ok().put("page", page);
    }

    /**
     * 获取当前品牌关联所有品牌分类列表
     * @param brandId
     * @return
     */
    @GetMapping("/catelog/list")
    // @RequiresPermissions("product:categorybrandrelation:list")
    public Result cateloglist(@RequestParam("brandId") Long brandId)
    {
        List<CategoryBrandRelationEntity> entities = categoryBrandRelationService
                .list(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
        
        return Result.ok().put("data", entities);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("product:categorybrandrelation:info")
    public Result info(@PathVariable("id") Long id)
    {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);
        
        return Result.ok().put("categoryBrandRelation", categoryBrandRelation);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:categorybrandrelation:save")
    public Result save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation)
    {
        categoryBrandRelationService.saveDetail(categoryBrandRelation);
        
        return Result.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    // @RequiresPermissions("product:categorybrandrelation:update")
    public Result update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation)
    {
        categoryBrandRelationService.updateById(categoryBrandRelation);
        
        return Result.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:categorybrandrelation:delete")
    public Result delete(@RequestBody Long[] ids)
    {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));
        
        return Result.ok();
    }
    
}
