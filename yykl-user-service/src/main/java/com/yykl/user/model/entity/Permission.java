// Permission.java
package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 权限实体类（对应数据库权限表）
 * 用于存储系统中的权限信息，如"user:add"、"product:delete"等
 */
@Data
@TableName("yykl_permission")
@Schema(description = "权限实体类，存储系统权限信息")
public class Permission {

    /**
     * 主键ID（自增）
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID，权限记录唯一标识，自增", example = "1")
    private Long id;

    /**
     * 权限标识（核心字段，如"user:add"、"product:query"）
     * 对应前端报错中需要的 getPermKey() 方法
     */
    @Schema(description = "权限唯一标识，如'user:add'表示用户新增权限", required = true, example = "user:add")
    private String permKey;

    /**
     * 权限名称（用于显示，如"用户新增权限"）
     */
    @Schema(description = "权限名称，用于显示", example = "用户新增权限")
    private String permName;

    /**
     * 权限描述（可选，如"允许新增系统用户"）
     */
    @Schema(description = "权限详细描述信息", example = "允许新增系统用户")
    private String description;

    /**
     * 权限对应的URL（可选，如"/api/user/add"）
     */
    @Schema(description = "权限对应的接口URL地址", example = "/api/user/add")
    private String url;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Schema(description = "权限状态，0表示禁用，1表示启用", example = "1")
    private Integer status;
}