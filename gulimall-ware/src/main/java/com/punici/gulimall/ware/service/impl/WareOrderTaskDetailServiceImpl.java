package com.punici.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.ware.dao.WareOrderTaskDetailDao;
import com.punici.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.punici.gulimall.ware.service.WareOrderTaskDetailService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("wareOrderTaskDetailService")
public class WareOrderTaskDetailServiceImpl extends ServiceImpl<WareOrderTaskDetailDao, WareOrderTaskDetailEntity>
        implements WareOrderTaskDetailService
{
    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<WareOrderTaskDetailEntity> page = this.page(new Query<WareOrderTaskDetailEntity>().getPage(params),
                new QueryWrapper<>());
        return new PageUtils(page);
    }
}