package com.punici.gulimall.product.controller;

import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;
import com.punici.gulimall.product.entity.AttrEntity;
import com.punici.gulimall.product.service.AttrService;
import com.punici.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Map;

/**
 * 商品属性
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@RestController
@RequestMapping("product/attr")
public class AttrController
{
    @Autowired
    private AttrService attrService;
    
    /**
     * 列表
     */
    @GetMapping("/base/list/{catelogId}")
    // @RequiresPermissions("product:attr:list")
    public Result baseAttrList(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId)
    {
        PageResult page = attrService.queryBaseAttrPage(params,catelogId);
        
        return Result.ok().put("page", page);
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:attr:list")
    public Result list(@RequestParam Map<String, Object> params)
    {
        PageResult page = attrService.queryPage(params);
        
        return Result.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    // @RequiresPermissions("product:attr:info")
    public Result info(@PathVariable("attrId") Long attrId)
    {
        AttrEntity attr = attrService.getById(attrId);
        
        return Result.ok().put("attr", attr);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attr:save")
    public Result save(@RequestBody AttrVo attr)
    {
        attrService.saveAttr(attr);
        
        return Result.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attr:update")
    public Result update(@RequestBody AttrEntity attr)
    {
        attrService.updateById(attr);
        
        return Result.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attr:delete")
    public Result delete(@RequestBody Long[] attrIds)
    {
        attrService.removeByIds(Arrays.asList(attrIds));
        
        return Result.ok();
    }
    
}
