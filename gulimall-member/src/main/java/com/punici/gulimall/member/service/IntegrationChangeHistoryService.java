package com.punici.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageResult;
import com.punici.gulimall.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:20:04
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageResult queryPage(Map<String, Object> params);
}

