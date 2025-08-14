package com.yykl.user.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yykl.user.model.entity.User;
import com.yykl.user.model.entity.Role;
import com.yykl.user.model.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户
    User selectByUsername(String username);

    // 根据用户ID查询角色列表
    List<Role> selectRolesByUserId(Long userId);

    // 根据用户ID查询权限列表
    List<Permission> selectPermissionsByUserId(Long userId);
}
