package com.yykl.product.service;

import com.yykl.product.model.entity.ProductCategory;

import java.util.List;

public interface CategoryService {
    ProductCategory createCategory(ProductCategory category);
    
    ProductCategory updateCategory(ProductCategory category);
    
    void deleteCategory(Long id);
    
    List<ProductCategory> getCategoryTree();
    
    void updateSort(List<Long> ids);
}