package com.punici.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.CommentReplayDao;
import com.punici.gulimall.product.entity.CommentReplayEntity;
import com.punici.gulimall.product.service.CommentReplayService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("commentReplayService")
public class CommentReplayServiceImpl extends ServiceImpl<CommentReplayDao, CommentReplayEntity> implements CommentReplayService
{
    
    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<CommentReplayEntity> page = this.page(new Query<CommentReplayEntity>().getPage(params), new QueryWrapper<>());
        return new PageUtils(page);
    }
}