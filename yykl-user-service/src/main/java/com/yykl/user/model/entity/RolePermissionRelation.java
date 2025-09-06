// RolePermissionRelation.java
package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 角色-权限关联实体类
 * 映射数据库表：t_role_permission_relation
 * 实现角色与权限的多对多关系
 */
@Data
@TableName("t_role_permission_relation")
@Schema(description = "角色-权限关联实体类，实现角色与权限的多对多关系")
public class RolePermissionRelation {

    /**
     * 角色ID
     * 外键，关联yykl_role表的id字段
     */
    @Schema(description = "角色ID，关联yykl_role表的id字段", example = "1")
    private Long roleId;

    /**
     * 权限ID
     * 外键，关联yykl_permission表的id字段
     */
    @Schema(description = "权限ID，关联yykl_permission表的id字段", example = "1")
    private Long permissionId;

    /**
     * 扩展字段：创建人ID
     * 记录是谁分配的该权限，便于审计
     */
    @Schema(description = "创建人ID，记录权限分配人", example = "1")
    private Long createBy;

    /**
     * 扩展字段：创建时间
     * 记录权限分配的时间
     */
    @Schema(description = "权限分配时间", example = "2023-10-01T12:00:00")
    private LocalDateTime createTime;
}