package com.yykl.product.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品表实体类
 */
@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 产品名称 */
    private String name;

    /** 关联分类ID */
    private Long category;
    
    /** 描述 */
    private String description;
    
    /** 价格 */
    private BigDecimal price;
    
    /** 封面图URL */
    private String coverUrl;
    
    /** 状态：0-下架，1-上架 */
    private Integer status;

    // 新增：关联的SKU列表（非数据库字段，用@TableField(exist = false)标识）
    @TableField(exist = false)
    private List<ProductSku> skus;

    // 新增：关联的库存信息（非数据库字段）
    @TableField(exist = false)
    private ProductStock stock;
}