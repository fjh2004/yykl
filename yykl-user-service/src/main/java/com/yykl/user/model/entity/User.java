package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer status;
}