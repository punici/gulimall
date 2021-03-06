package com.punici.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.punici.gulimall.ware.entity.WareOrderTaskEntity;
import com.punici.gulimall.ware.service.WareOrderTaskService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;



/**
 * 库存工作单
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
@RestController
@RequestMapping("ware/wareordertask")
public class WareOrderTaskController {
    @Autowired
    private WareOrderTaskService wareOrderTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:wareordertask:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageResult page = wareOrderTaskService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:wareordertask:info")
    public Result info(@PathVariable("id") Long id){
		WareOrderTaskEntity wareOrderTask = wareOrderTaskService.getById(id);

        return Result.ok().put("wareOrderTask", wareOrderTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:wareordertask:save")
    public Result save(@RequestBody WareOrderTaskEntity wareOrderTask){
		wareOrderTaskService.save(wareOrderTask);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:wareordertask:update")
    public Result update(@RequestBody WareOrderTaskEntity wareOrderTask){
		wareOrderTaskService.updateById(wareOrderTask);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:wareordertask:delete")
    public Result delete(@RequestBody Long[] ids){
		wareOrderTaskService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
