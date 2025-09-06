package com.yykl.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yykl.product.mapper.ProductCategoryMapper;
import com.yykl.product.model.entity.ProductCategory;
import com.yykl.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    @Transactional
    public ProductCategory createCategory(ProductCategory category) {
        // 计算分类级别
        if (category.getParentId() == null || category.getParentId() == 0) {
            category.setLevel(1); // 一级分类
        } else {
            ProductCategory parent = productCategoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new RuntimeException("父分类不存在");
            }
            category.setLevel(parent.getLevel() + 1);
        }

        // 设置排序值（默认在同级最后）
        if (category.getSort() == null) {
            QueryWrapper<ProductCategory> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", category.getParentId())
                    .orderByDesc("sort")
                    .last("limit 1");
            ProductCategory lastCategory = productCategoryMapper.selectOne(wrapper);
            category.setSort(lastCategory != null ? lastCategory.getSort() + 10 : 10);
        }

        productCategoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public ProductCategory updateCategory(ProductCategory category) {
        productCategoryMapper.updateById(category);
        return category;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // 检查是否有子分类
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        int count = Math.toIntExact(productCategoryMapper.selectCount(wrapper));
        if (count > 0) {
            throw new RuntimeException("该分类下存在子分类，不能删除");
        }

        productCategoryMapper.deleteById(id);
    }

    @Override
    public List<ProductCategory> getCategoryTree() {
        // 1. 获取所有分类
        List<ProductCategory> allCategories = productCategoryMapper.selectList(null);

        // 2. 构建分类树
        return buildCategoryTree(allCategories, 0L);
    }

    /**
     * 递归构建分类树
     */
    private List<ProductCategory> buildCategoryTree(List<ProductCategory> allCategories, Long parentId) {
        List<ProductCategory> children = allCategories.stream()
                .filter(cat -> parentId.equals(cat.getParentId()))
                .sorted((c1, c2) -> c1.getSort().compareTo(c2.getSort()))
                .collect(Collectors.toList());

        for (ProductCategory child : children) {
            child.setChildren(buildCategoryTree(allCategories, child.getId()));
        }

        return children;
    }

    @Override
    @Transactional
    public void updateSort(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        // 批量更新排序，每间隔10设置一个排序值，便于后续插入
        for (int i = 0; i < ids.size(); i++) {
            ProductCategory category = new ProductCategory();
            category.setId(ids.get(i));
            category.setSort((i + 1) * 10);
            productCategoryMapper.updateById(category);
        }
    }
}