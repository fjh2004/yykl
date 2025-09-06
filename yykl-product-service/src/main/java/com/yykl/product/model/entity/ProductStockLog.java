package com.yykl.product.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("product_stock_log")
public class ProductStockLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private Long skuId;

    private String operationType; // LOCK/DEDUCT/UNLOCK

    private Integer quantity;

    private String orderId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}