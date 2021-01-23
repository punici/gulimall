package com.punici.gulimall.coupon.controller;

import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;
import com.punici.gulimall.coupon.entity.CouponEntity;
import com.punici.gulimall.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 优惠券信息
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:06:20
 */
@RestController
@RefreshScope
@RequestMapping("coupon/coupon")
public class CouponController
{
    @Autowired
    private CouponService couponService;
    
    @Value("${gulimall.user}")
    String user;
    
    @Value("${gulimall.age}")
    int age;
    
    @GetMapping("test")
    public Result test()
    {
        return Objects.requireNonNull(Result.ok().put("name", user)).put("age", age);
    }
    
    @GetMapping("/member/coupons")
    public Result memberCoupons()
    {
        CouponEntity entity = new CouponEntity();
        entity.setId(0L);
        entity.setCouponType(0);
        entity.setCouponImg("");
        entity.setCouponName("10086");
        return Result.ok().put("coupons", Arrays.asList(entity));
        
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:coupon:list")
    public Result list(@RequestParam Map<String, Object> params)
    {
        PageResult page = couponService.queryPage(params);
        
        return Result.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("coupon:coupon:info")
    public Result info(@PathVariable("id") Long id)
    {
        CouponEntity coupon = couponService.getById(id);
        
        return Result.ok().put("coupon", coupon);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:coupon:save")
    public Result save(@RequestBody CouponEntity coupon)
    {
        couponService.save(coupon);
        
        return Result.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:coupon:update")
    public Result update(@RequestBody CouponEntity coupon)
    {
        couponService.updateById(coupon);
        
        return Result.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("coupon:coupon:delete")
    public Result delete(@RequestBody Long[] ids)
    {
        couponService.removeByIds(Arrays.asList(ids));
        
        return Result.ok();
    }
    
}
