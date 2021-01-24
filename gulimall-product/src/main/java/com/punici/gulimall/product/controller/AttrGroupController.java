package com.punici.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;
import com.punici.gulimall.product.entity.AttrEntity;
import com.punici.gulimall.product.entity.AttrGroupEntity;
import com.punici.gulimall.product.service.AttrGroupService;
import com.punici.gulimall.product.service.AttrService;
import com.punici.gulimall.product.service.CategoryService;
import com.punici.gulimall.product.vo.AttrGroupRelationVo;

/**
 * 属性分组
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController
{
    @Autowired
    private AttrGroupService attrGroupService;
    
    @Autowired
    private AttrService attrService;
    
    @Autowired
    private CategoryService categoryService;
    
    /// product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public Result attrRelation(@PathVariable("attrgroupId") Long attrgroupId)
    {
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return Result.ok().put("data", entities);
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    // @RequiresPermissions("product:attrgroup:list")
    public Result list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId)
    {
        // PageUtils page = attrGroupService.queryPage(params);
        PageResult page = attrGroupService.queryPage(params, catelogId);
        return Result.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    // @RequiresPermissions("product:attrgroup:info")
    public Result info(@PathVariable("attrGroupId") Long attrGroupId)
    {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        
        Long[] path = categoryService.findCategoryPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(path);
        return Result.ok().put("attrGroup", attrGroup);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attrgroup:save")
    public Result save(@RequestBody AttrGroupEntity attrGroup)
    {
        attrGroupService.save(attrGroup);
        
        return Result.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attrgroup:update")
    public Result update(@RequestBody AttrGroupEntity attrGroup)
    {
        attrGroupService.updateById(attrGroup);
        
        return Result.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attrgroup:delete")
    public Result delete(@RequestBody Long[] attrGroupIds)
    {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        
        return Result.ok();
    }
    
    @PostMapping("/attr/relation/delete")
    public Result deleteRelation(@RequestBody AttrGroupRelationVo[] vos)
    {
        attrService.deleteRelation(vos);
        return Result.ok();
    }
    
}
