package com.punici.gulimall.member.feign;

import com.punici.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @GetMapping("/coupon/coupon/member/coupons")
    R memberCoupons();
}
