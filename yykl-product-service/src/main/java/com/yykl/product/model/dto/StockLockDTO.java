package com.yykl.product.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StockLockDTO {
    private String orderId;
    
    private List<StockItem> items;



    @Data
    public static class StockItem {
        private  Long productId;
        
        private Long skuId;
        
        private Integer quantity;
    }
}