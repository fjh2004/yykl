package com.yykl.product.controller;

import com.yykl.product.model.entity.Product;
import com.yykl.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> listProducts(@RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) String tag,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        // 传递排序参数
        return productService.listProductsByCategory(categoryId, sort);
    }

    @GetMapping("/{id}")
    public Product getProductDetail(@PathVariable Long id) {
        return productService.getProductDetail(id);
    }

    @GetMapping("/scene")
    public List<Product> listProductsByScene(@RequestParam String tag,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return productService.listProductsByScene(tag);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return productService.searchProducts(keyword);
    }
}