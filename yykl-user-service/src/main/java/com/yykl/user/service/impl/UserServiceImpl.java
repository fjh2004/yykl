package com.yykl.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yykl.user.mapper.UserMapper;
import com.yykl.user.model.entity.User;
import com.yykl.user.model.request.RegisterRequest;
import com.yykl.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest registerRequest) {
        // 1. 检查用户名是否已存在
        User existingUser = baseMapper.selectByUsername(registerRequest.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 构建用户对象并加密密码
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 加密密码
        user.setPhone(registerRequest.getPhone());
        user.setNickname(registerRequest.getNickname() != null ? registerRequest.getNickname() : registerRequest.getUsername());
        user.setStatus(1); // 默认为正常状态

        // 3. 保存用户到数据库
        baseMapper.insert(user);
    }
    @Override
    public User selectByUsername(String username) {
        // 直接调用Mapper层的selectByUsername方法
        return baseMapper.selectByUsername(username);
    }
}