package com.punici.gulimall.ware.controller;

import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.R;
import com.punici.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.punici.gulimall.ware.service.WareOrderTaskDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Map;

/**
 * 库存工作单
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
@RestController
@RequestMapping("ware/wareordertaskdetail")
public class WareOrderTaskDetailController
{
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("ware:wareordertaskdetail:list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = wareOrderTaskDetailService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("ware:wareordertaskdetail:info")
    public R info(@PathVariable("id") Long id)
    {
        WareOrderTaskDetailEntity wareOrderTaskDetail = wareOrderTaskDetailService.getById(id);
        return R.ok().put("wareOrderTaskDetail", wareOrderTaskDetail);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("ware:wareordertaskdetail:save")
    public R save(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail)
    {
        wareOrderTaskDetailService.save(wareOrderTaskDetail);
        return R.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("ware:wareordertaskdetail:update")
    public R update(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail)
    {
        wareOrderTaskDetailService.updateById(wareOrderTaskDetail);
        return R.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("ware:wareordertaskdetail:delete")
    public R delete(@RequestBody Long[] ids)
    {
        wareOrderTaskDetailService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
