package com.yykl.user.controller;

import com.yykl.user.model.entity.User;
import com.yykl.user.model.request.UpdateUserRequest;
import com.yykl.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 获取当前登录用户信息
    @GetMapping("/info")
    public ResponseEntity<User> getCurrentUser() {
        // 从Security上下文获取当前登录用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 查询用户信息
        User user = userService.selectByUsername(username);
        // 敏感信息脱敏
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // 修改个人信息
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody UpdateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.selectByUsername(username);

        // 更新可修改的字段
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        userService.updateById(user);

        Map<String, String> result = new HashMap<>();
        result.put("code", "200");
        result.put("msg", "修改成功");
        return ResponseEntity.ok(result);
    }
}