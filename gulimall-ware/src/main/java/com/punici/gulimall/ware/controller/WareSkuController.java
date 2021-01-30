package com.punici.gulimall.ware.controller;

import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.R;
import com.punici.gulimall.ware.entity.WareSkuEntity;
import com.punici.gulimall.ware.service.WareSkuService;
import com.punici.gulimall.ware.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController
{
    @Autowired
    private WareSkuService wareSkuService;
    
    // 查询sku是否有库存
    @PostMapping("/hasstock")
    public R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> skuId)
    {
        List<SkuHasStockVo> skuHasStockVos = wareSkuService.getSkuHasStock(skuId);
        return R.ok().setData(skuHasStockVos);
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = wareSkuService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id)
    {
        WareSkuEntity wareSku = wareSkuService.getById(id);
        return R.ok().put("wareSku", wareSku);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku)
    {
        wareSkuService.save(wareSku);
        return R.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku)
    {
        wareSkuService.updateById(wareSku);
        return R.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids)
    {
        wareSkuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
