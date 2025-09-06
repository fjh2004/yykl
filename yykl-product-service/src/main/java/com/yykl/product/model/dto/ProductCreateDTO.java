package com.yykl.product.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateDTO {
    private String name;
    
    private String subtitle;
    
    private Long categoryId;
    
    private BigDecimal price;
    
    private String description;
    
    private String tags;
    
    private String scenes;
    
    private List<SkuDTO> skus;
    
    @Data
    public static class SkuDTO {
        private String name;
        
        private BigDecimal price;
        
        private String spec;
        
        private Integer stock;
    }
}