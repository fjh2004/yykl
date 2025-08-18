package com.yykl.order.feign;

import com.yykl.order.model.dto.ProductStockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Component
@FeignClient(name = "yykl-resource-service")
public interface ProductFeignClient {
    
    @GetMapping("/api/product/{productId}/stock")
    Integer getProductStock(@PathVariable("productId") Long productId);

    @PutMapping("/api/product/stock")
    Boolean deductStock(@RequestBody ProductStockDTO stockDTO);
}