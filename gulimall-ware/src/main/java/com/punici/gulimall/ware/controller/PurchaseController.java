package com.punici.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.punici.gulimall.ware.entity.PurchaseEntity;
import com.punici.gulimall.ware.service.PurchaseService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;



/**
 * 采购信息
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageResult page = purchaseService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public Result info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return Result.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public Result save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public Result update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public Result delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
