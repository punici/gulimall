package com.punici.gulimall.order.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.punici.gulimall.order.entity.OrderEntity;
import com.punici.gulimall.order.service.OrderService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;



/**
 * 订单
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:30:29
 */
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("order:order:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageResult page = orderService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("order:order:info")
    public Result info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return Result.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:order:save")
    public Result save(@RequestBody OrderEntity order){
		orderService.save(order);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("order:order:update")
    public Result update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("order:order:delete")
    public Result delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
