package com.yykl.user.service.impl;

import com.yykl.user.mapper.UserMapper;
import com.yykl.user.model.entity.User;
import com.yykl.user.model.entity.Role;
import com.yykl.user.model.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<Role> roles = userMapper.selectRolesByUserId(user.getId());
        List<Permission> permissions = userMapper.selectPermissionsByUserId(user.getId());

        List<GrantedAuthority> authorities = permissions.stream()
            .map(perm -> new SimpleGrantedAuthority(perm.getPermKey()))
            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getStatus() == 1,
            true, true, true,
            authorities
        );
    }
}