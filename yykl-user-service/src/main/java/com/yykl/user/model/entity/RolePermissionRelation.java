package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 角色-权限关联实体类
 * 映射数据库表：t_role_permission_relation
 * 实现角色与权限的多对多关系
 */
@Data
@TableName("t_role_permission_relation")
public class RolePermissionRelation {

    /**
     * 角色ID
     * 外键，关联yykl_role表的id字段
     */
    private Long roleId;

    /**
     * 权限ID
     * 外键，关联yykl_permission表的id字段
     */
    private Long permissionId;

    /**
     * 扩展字段：创建人ID
     * 记录是谁分配的该权限，便于审计
     */
    private Long createBy;

    /**
     * 扩展字段：创建时间
     * 记录权限分配的时间
     */
    private LocalDateTime createTime;
}
