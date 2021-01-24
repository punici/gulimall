package com.punici.gulimall.coupon.controller;

import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.R;
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
    public R test()
    {
        return Objects.requireNonNull(R.ok().put("name", user)).put("age", age);
    }
    
    @GetMapping("/member/coupons")
    public R memberCoupons()
    {
        CouponEntity entity = new CouponEntity();
        entity.setId(0L);
        entity.setCouponType(0);
        entity.setCouponImg("");
        entity.setCouponName("10086");
        return R.ok().put("coupons", Arrays.asList(entity));
        
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:coupon:list")
    public R list(@RequestParam Map<String, Object> params)
    {
        PageUtils page = couponService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("coupon:coupon:info")
    public R info(@PathVariable("id") Long id)
    {
        CouponEntity coupon = couponService.getById(id);
        
        return R.ok().put("coupon", coupon);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:coupon:save")
    public R save(@RequestBody CouponEntity coupon)
    {
        couponService.save(coupon);
        
        return R.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:coupon:update")
    public R update(@RequestBody CouponEntity coupon)
    {
        couponService.updateById(coupon);
        
        return R.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("coupon:coupon:delete")
    public R delete(@RequestBody Long[] ids)
    {
        couponService.removeByIds(Arrays.asList(ids));
        
        return R.ok();
    }
    
}
