// User.java
package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户实体类
 * <p>
 * 表结构：
 * CREATE TABLE `yykl_user` (
 *   `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
 *   `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '登录账号',
 *   `password` VARCHAR(100) NOT NULL COMMENT '加密后的密码',
 *   `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
 *   `nickname` VARCHAR(50) COMMENT '用户昵称',
 *   `avatar` VARCHAR(255) COMMENT '头像地址',
 *   `status` TINYINT DEFAULT 1 COMMENT '状态(0-禁用 1-正常)',
 *   `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
 * );
 */
@Data
@TableName("yykl_user")
@Schema(description = "用户实体类，存储系统用户信息")
public class User {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID，用户记录唯一标识，自增", example = "1")
    private Long id;

    @Schema(description = "登录账号，唯一", required = true, example = "admin")
    private String username;

    @Schema(description = "加密后的登录密码", required = true, example = "$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
    private String password;

    @Schema(description = "用户手机号，唯一", example = "13800138000")
    private String phone;

    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Schema(description = "用户头像URL地址", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "用户状态，0表示禁用，1表示正常", example = "1")
    private Integer status;
}