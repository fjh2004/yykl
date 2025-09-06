// Role.java
package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "角色实体类，存储系统角色信息")
public class Role {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID，角色记录唯一标识，自增", example = "1")
    private Long id;

    @Schema(description = "角色名称", required = true, example = "系统管理员")
    private String roleName;

    @Schema(description = "角色标识，如admin、merchant", required = true, example = "admin")
    private String roleKey;

    @Schema(description = "角色功能描述", example = "拥有系统全部操作权限")
    private String description;
}