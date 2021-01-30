package com.punici.gulimall.search.controller;

import com.punici.gulimall.common.exception.BizCodeEnume;
import com.punici.gulimall.common.to.es.SkuEsModel;
import com.punici.gulimall.common.utils.R;
import com.punici.gulimall.search.service.ElasticProductSaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search/save")
@RestController
public class ElasticSaveController
{
    @Autowired(required = false)
    ElasticProductSaveService productSaveService;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    // 上架商品
    @PostMapping("")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels)
    {
        boolean b = false;
        try
        {
            b = productSaveService.productSaveStatusUp(skuEsModels);
            
            if(!b)
            {
                return R.ok();
            }
            else
            {
                return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg());
            }
        }
        
        catch (Exception e)
        {
            logger.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg(), e);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg());
        }
        
    }
}
