package com.yykl.product.controller;

import com.yykl.product.model.dto.StockLockDTO;
import com.yykl.product.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/product")
public class InternalProductController {
    @Autowired
    private StockService stockService;

    @PostMapping("/stock/lock")
    public boolean lockStock(@RequestBody StockLockDTO stockLockDTO) {
        return stockService.lockStock(stockLockDTO);
    }

    @PostMapping("/stock/deduct")
    public boolean deductStock(@RequestParam String orderId) {
        return stockService.deductStock(orderId);
    }

    @PostMapping("/stock/unlock")
    public boolean unlockStock(@RequestParam String orderId) {
        return stockService.unlockStock(orderId);
    }

    @GetMapping("/info")
    public void getProductInfo(@RequestParam String productIds, 
                            @RequestParam String skuIds) {
        // 实现产品信息查询逻辑
    }
}