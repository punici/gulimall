package com.punici.gulimall.product.controller;

import com.punici.gulimall.common.utils.Result;
import com.punici.gulimall.product.entity.CategoryEntity;
import com.punici.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@RestController
@RequestMapping("product/category")
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 查出所有分类以及子分类列表，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    // @RequiresPermissions("product:category:list")
    public Result list(@RequestParam Map<String, Object> params)
    {
        List<CategoryEntity> entities = categoryService.listWithTree();
        return Result.ok().put("data", entities);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    // @RequiresPermissions("product:category:info")
    public Result info(@PathVariable("catId") Long catId)
    {
        CategoryEntity category = categoryService.getById(catId);
        
        return Result.ok().put("data", category);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:category:save")
    public Result save(@RequestBody CategoryEntity category)
    {
        categoryService.save(category);
        
        return Result.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:category:update")
    public Result update(@RequestBody CategoryEntity category)
    {
        categoryService.updateById(category);
        return Result.ok();
    }
    
    /**
     * 删除
     * @RequestBody:获取请求体，必须发送post请求才有 get请求没有 SpringMvc 自动将请求体的数据 ( json ) 转为对应的对象
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:category:delete")
    public Result delete(@RequestBody Long[] catIds)
    {
        // categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(catIds);
        return Result.ok();
    }
    
}
