package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户-角色关联表
 * 实现RBAC权限模型的多对多关联
 */
@Data
@TableName("t_user_role_relation")
public class UserRoleRelation {
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 角色ID
     */
    private Long roleId;
}