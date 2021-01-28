package com.punici.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.product.entity.SpuInfoEntity;
import com.punici.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
public interface SpuInfoService extends IService<SpuInfoEntity>
{
    
    PageUtils queryPage(Map<String, Object> params);
    
    void saveSpuInfo(SpuSaveVo vo);
    
    void saveBaseSpuInfo(SpuInfoEntity infoEntity);
    
    PageUtils queryPageByCondition(Map<String, Object> params);
    
    void up(Long spuId);
}
