package com.yykl.order.model.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class OrderCreateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "商品ID不能为空")
    private String productId;
    
    @Min(value = 1, message = "购买数量至少为1")
    private Integer quantity;
    
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal totalFee;
    
    @NotBlank(message = "收货地址不能为空")
    private String deliveryAddress;
}