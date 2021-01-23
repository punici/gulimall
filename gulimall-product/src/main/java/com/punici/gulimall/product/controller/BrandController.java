package com.punici.gulimall.product.controller;

import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;
import com.punici.gulimall.common.valid.AddGroup;
import com.punici.gulimall.common.valid.UpdateStatusGroup;
import com.punici.gulimall.product.entity.BrandEntity;
import com.punici.gulimall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 品牌
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@RestController
@RequestMapping("product/brand")
public class BrandController
{
    @Autowired
    private BrandService brandService;
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:brand:list")
    public Result list(@RequestParam Map<String, Object> params)
    {
        PageResult page = brandService.queryPage(params);
        
        return Result.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    // @RequiresPermissions("product:brand:info")
    public Result info(@PathVariable("brandId") Long brandId)
    {
        BrandEntity brand = brandService.getById(brandId);
        
        return Result.ok().put("brand", brand);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:brand:save")
    public Result save(@Validated(AddGroup.class) @RequestBody BrandEntity brand)//BindingResult result)
    {
//        Map<String, String> errorMap = new HashMap<>();
//        if(result.hasErrors())
//        {
//            // 获取校验的错误结果
//            result.getFieldErrors().forEach((i) -> {
//                // 获取错误的提示
//                String defaultMessage = i.getDefaultMessage();
//                // 获取错误的属性名字
//                String field = i.getField();
//                errorMap.put(field, defaultMessage);
//            });
//            return R.error(400, "提交的数据不合法").put("data", errorMap);
//        }
        brandService.save(brand);
        
        return Result.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    // @RequiresPermissions("product:brand:update")
    public Result update(@RequestBody BrandEntity brand)
    {
        brandService.updateDetail(brand);
        return Result.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    // @RequiresPermissions("product:brand:update")
    public Result updateBrandStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand)
    {
        brandService.updateById(brand);

        return Result.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:brand:delete")
    public Result delete(@RequestBody Long[] brandIds)
    {
        brandService.removeByIds(Arrays.asList(brandIds));
        
        return Result.ok();
    }
    
}
