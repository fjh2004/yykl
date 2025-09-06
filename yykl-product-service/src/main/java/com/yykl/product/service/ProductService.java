package com.yykl.product.service;

import com.yykl.product.model.dto.ProductCreateDTO;
import com.yykl.product.model.entity.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(ProductCreateDTO productCreateDTO);

    Product updateProduct(Long id, ProductCreateDTO productCreateDTO);

    void updateStatus(Long id, Integer status);

    // 添加排序参数
    List<Product> listProductsByCategory(Long categoryId, String sort);

    Product getProductDetail(Long id);

    List<Product> listProductsByScene(String tag);

    List<Product> searchProducts(String keyword);
}