package com.punici.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.ware.entity.PurchaseEntity;
import com.punici.gulimall.ware.vo.MergeVo;
import com.punici.gulimall.ware.vo.PurchaseDoneVo;
import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
public interface PurchaseService extends IService<PurchaseEntity>
{
    
    PageUtils queryPage(Map<String, Object> params);
    
    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);
    
    void mergePurchase(MergeVo mergeVo);
    
    void received(List<Long> ids);
    
    void done(PurchaseDoneVo doneVo);
    
}
