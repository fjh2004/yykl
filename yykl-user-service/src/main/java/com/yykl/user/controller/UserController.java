package com.yykl.user.controller;

import com.yykl.user.model.entity.User;
import com.yykl.user.model.request.UpdateUserRequest;
import com.yykl.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "用户信息接口", description = "用户信息查询及修改相关接口") // 类级标签
public class UserController {

    @Autowired
    private UserService userService;

    // 获取当前登录用户信息
    @GetMapping("/info")
    @Operation(summary = "获取当前登录用户信息", description = "返回当前用户的基本信息（密码已脱敏）")
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
    @Operation(summary = "更新用户信息", description = "修改当前登录用户的昵称、电话或头像")
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