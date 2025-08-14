package com.yykl.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yykl.user.model.entity.User;
import com.yykl.user.model.request.RegisterRequest;

public interface UserService extends IService<User> {
    // 注册用户
    void register(RegisterRequest registerRequest);
    User selectByUsername(String username);
}