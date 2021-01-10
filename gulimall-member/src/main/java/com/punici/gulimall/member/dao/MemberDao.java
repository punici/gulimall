package com.punici.gulimall.member.dao;

import com.punici.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:20:04
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
