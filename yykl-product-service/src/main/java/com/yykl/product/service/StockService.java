package com.yykl.product.service;

import com.yykl.product.model.dto.StockLockDTO;

public interface StockService {
    boolean lockStock(StockLockDTO stockLockDTO);
    
    boolean deductStock(String orderId);
    
    boolean unlockStock(String orderId);
}