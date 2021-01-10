package com.punici.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:20:04
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

