package com.punici.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购信息
 * 
 * @author punici
 * @email punici@163.com
 * @date 2021-01-10 21:35:05
 */
@Data
@TableName("wms_purchase")
public class PurchaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 采购单id
     */
    @TableId
    private Long id;
    
    /**
     * 采购人id
     */
    private Long assigneeId;
    
    /**
     * 采购人名
     */
    private String assigneeName;
    
    /**
     * 联系方式
     */
    private String phone;
    
    /**
     * 优先级
     */
    private Integer priority;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 仓库id
     */
    private Long wareId;
    
    /**
     * 总金额
     */
    private BigDecimal amount;
    
    /**
     * 创建日期
     */
    private Date createTime;
    
    /**
     * 更新日期
     */
    private Date updateTime;
    
}
