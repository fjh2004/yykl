package com.yykl.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yykl.product.mapper.ProductMapper;
import com.yykl.product.mapper.ProductSkuMapper;
import com.yykl.product.mapper.ProductStockMapper;
import com.yykl.product.model.dto.ProductCreateDTO;
import com.yykl.product.model.entity.Product;
import com.yykl.product.model.entity.ProductSku;
import com.yykl.product.model.entity.ProductStock;
import com.yykl.product.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Override
    @Transactional
    public Product createProduct(ProductCreateDTO productCreateDTO) {
        // 1. 保存产品基本信息
        Product product = new Product();
        BeanUtils.copyProperties(productCreateDTO, product);
        product.setStatus(1); // 默认上架状态
        productMapper.insert(product);

        // 2. 保存SKU和库存信息
        if (productCreateDTO.getSkus() != null && !productCreateDTO.getSkus().isEmpty()) {
            int totalStock = 0;

            for (ProductCreateDTO.SkuDTO skuDTO : productCreateDTO.getSkus()) {
                // 保存SKU
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(skuDTO, sku);
                sku.setProductId(product.getId());
                productSkuMapper.insert(sku);

                // 累计库存
                totalStock += skuDTO.getStock();
            }

            // 保存库存信息
            ProductStock stock = new ProductStock();
            stock.setProductId(product.getId());
            stock.setQuantity(totalStock);
            stock.setLockedStock(0);
            stock.setUpdateTime(new Date());
            productStockMapper.insert(stock);
        }

        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductCreateDTO productCreateDTO) {
        // 1. 查询产品是否存在
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("产品不存在");
        }

        // 2. 更新产品基本信息
        BeanUtils.copyProperties(productCreateDTO, product);
        productMapper.updateById(product);

        // 3. 先删除原有SKU和库存，再重新添加（实际项目中可考虑增量更新）
        QueryWrapper<ProductSku> skuWrapper = new QueryWrapper<>();
        skuWrapper.eq("product_id", id);
        productSkuMapper.delete(skuWrapper);

        QueryWrapper<ProductStock> stockWrapper = new QueryWrapper<>();
        stockWrapper.eq("product_id", id);
        productStockMapper.delete(stockWrapper);

        // 4. 重新保存SKU和库存
        if (productCreateDTO.getSkus() != null && !productCreateDTO.getSkus().isEmpty()) {
            int totalStock = 0;

            for (ProductCreateDTO.SkuDTO skuDTO : productCreateDTO.getSkus()) {
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(skuDTO, sku);
                sku.setProductId(id);
                productSkuMapper.insert(sku);

                totalStock += skuDTO.getStock();
            }

            ProductStock stock = new ProductStock();
            stock.setProductId(id);
            stock.setQuantity(totalStock);
            stock.setLockedStock(0);
            stock.setUpdateTime(new Date());
            productStockMapper.insert(stock);
        }

        return product;
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        productMapper.updateById(product);
    }

    @Override
    public List<Product> listProductsByCategory(Long categoryId, String sort) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();

        // 分类筛选
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }

        // 只查询上架产品
        wrapper.eq("status", 1);

        // 排序处理
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                String field = sortParams[0];
                String direction = sortParams[1].toUpperCase();

                // 支持的排序字段
                if ("price".equals(field)) {
                    if ("ASC".equals(direction)) {
                        wrapper.orderByAsc("price");
                    } else {
                        wrapper.orderByDesc("price");
                    }
                } else if ("create_time".equals(field)) {
                    if ("ASC".equals(direction)) {
                        wrapper.orderByAsc("create_time");
                    } else {
                        wrapper.orderByDesc("create_time");
                    }
                }
            }
        } else {
            // 默认排序
            wrapper.orderByDesc("create_time");
        }

        return productMapper.selectList(wrapper);
    }

    @Override
    public Product getProductDetail(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<Product> listProductsByScene(String tag) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .like("tags", tag);
        return productMapper.selectList(wrapper);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .and(qw -> qw.like("name", keyword)
                        .or().like("description", keyword)
                        .or().like("tags", keyword));
        return productMapper.selectList(wrapper);
    }
}