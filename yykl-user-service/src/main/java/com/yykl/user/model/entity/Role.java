package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 角色实体类
 * 表结构：
 * CREATE TABLE `yykl_role` (
 *   `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
 *   `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
 *   `role_key` VARCHAR(50) UNIQUE NOT NULL COMMENT '角色标识(如admin,merchant)',
 *   `description` VARCHAR(255) COMMENT '角色描述'
 * );
 */
@Data
@TableName("yykl_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String roleName;
    private String roleKey;
    private String description;
}