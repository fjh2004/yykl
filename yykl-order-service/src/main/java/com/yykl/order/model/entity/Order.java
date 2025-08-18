package com.yykl.order.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_order")
public class Order {
    @TableId
    private String orderNo;
    private Long userId;
    private Integer orderState;
    private BigDecimal totalAmount;
    private Date createTime;
    private Date updateTime;
    private String productId;
    private Integer quantity;
}