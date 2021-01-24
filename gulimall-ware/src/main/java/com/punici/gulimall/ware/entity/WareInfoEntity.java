package com.punici.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 仓库信息
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
@Data
@TableName("wms_ware_info")
public class WareInfoEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
     * id
     */
    @TableId
    private Long id;
    
    /**
     * 仓库名
     */
    private String name;
    
    /**
     * 仓库地址
     */
    private String address;
    
    /**
     * 区域编码
     */
    private String areacode;
    
}
