// UserRoleRelation.java
package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户-角色关联表
 * 实现RBAC权限模型的多对多关联
 */
@Data
@TableName("t_user_role_relation")
@Schema(description = "用户-角色关联实体类，实现用户与角色的多对多关系")
public class UserRoleRelation {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID，关联yykl_user表的id字段", example = "1")
    private Long userId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID，关联yykl_role表的id字段", example = "1")
    private Long roleId;
}