package com.yykl.product.controller;

import com.yykl.product.model.dto.ProductCreateDTO;
import com.yykl.product.model.entity.Product;
import com.yykl.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public Product createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        return productService.createProduct(productCreateDTO);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, 
                               @RequestBody ProductCreateDTO productCreateDTO) {
        return productService.updateProduct(id, productCreateDTO);
    }

    @PutMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        productService.updateStatus(id, status);
    }
}