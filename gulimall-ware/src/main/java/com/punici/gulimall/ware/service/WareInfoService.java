package com.punici.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.ware.entity.WareInfoEntity;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
public interface WareInfoService extends IService<WareInfoEntity>
{
    
    PageUtils queryPage(Map<String, Object> params);
}
