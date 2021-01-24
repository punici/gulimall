package com.punici.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * spu信息介绍
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 19:53:51
 */
@Data
@TableName("pms_spu_info_desc")
public class SpuInfoDescEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 商品id
     */
    @TableId
    private Long spuId;
    
    /**
     * 商品介绍
     */
    private String decript;
    
}
