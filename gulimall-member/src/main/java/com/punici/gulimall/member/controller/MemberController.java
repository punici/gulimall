package com.punici.gulimall.member.controller;

import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.common.utils.Result;
import com.punici.gulimall.member.entity.MemberEntity;
import com.punici.gulimall.member.feign.CouponFeignService;
import com.punici.gulimall.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;


/**
 * 会员
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:20:04
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeignService couponFeignService;

    @GetMapping("/test")
    public Result test(){
        MemberEntity memberEntity=new MemberEntity();
        memberEntity.setNickname("张三李四");
        Result r = couponFeignService.memberCoupons();
        return Objects.requireNonNull(Result.ok().put("member", memberEntity)).put("coupons",r.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageResult page = memberService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public Result info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return Result.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public Result save(@RequestBody MemberEntity member){
		memberService.save(member);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public Result update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public Result delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
