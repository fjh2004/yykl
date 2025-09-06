package com.yykl.product.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 产品库存表实体类
 */
@Data
@TableName("product_stock")
public class ProductStock {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 关联产品ID */
    private Long productId;
    
    /** 库存数量 */
    private Integer quantity;
    
    /** 锁定数量 */
    private Integer lockedStock;
    
    /** 更新时间 */
    private Date updateTime;
}