package com.punici.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.ware.dao.WareOrderTaskDao;
import com.punici.gulimall.ware.entity.WareOrderTaskEntity;
import com.punici.gulimall.ware.service.WareOrderTaskService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskDao, WareOrderTaskEntity> implements WareOrderTaskService
{
    
    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<WareOrderTaskEntity> page = this.page(new Query<WareOrderTaskEntity>().getPage(params), new QueryWrapper<>());
        return new PageUtils(page);
    }
}