package com.yykl.user.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限实体类（对应数据库权限表）
 * 用于存储系统中的权限信息，如"user:add"、"product:delete"等
 */
@Data
@TableName("yykl_permission") // 绑定数据库表名（根据你的实际表名修改）
public class Permission {

    /**
     * 主键ID（自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 权限标识（核心字段，如"user:add"、"product:query"）
     * 对应前端报错中需要的 getPermKey() 方法
     */
    private String permKey;

    /**
     * 权限名称（用于显示，如"用户新增权限"）
     */
    private String permName;

    /**
     * 权限描述（可选，如"允许新增系统用户"）
     */
    private String description;

    /**
     * 权限对应的URL（可选，如"/api/user/add"）
     */
    private String url;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
}
