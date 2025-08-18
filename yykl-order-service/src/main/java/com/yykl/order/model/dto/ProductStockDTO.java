package com.yykl.order.model.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ProductStockDTO {
    @NotBlank(message = "商品ID不能为空")
    private String productId;
    
    @Min(value = 1, message = "扣减数量必须大于0")
    private Integer quantity;
}