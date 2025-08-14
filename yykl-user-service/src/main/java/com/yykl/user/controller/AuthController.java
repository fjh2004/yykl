
package com.yykl.user.controller;

import com.yykl.user.model.request.LoginRequest;
import com.yykl.user.model.request.RegisterRequest;
import com.yykl.user.service.UserService;
import com.yykl.user.service.impl.UserDetailsServiceImpl;
import com.yykl.user.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        // 1. 调用Spring Security认证管理器进行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. 认证通过后，获取用户权限并生成JWT
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtUtils.generateToken(loginRequest.getUsername(), roles);

        // 3. 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "登录成功");
        result.put("token", token);
        result.put("username", loginRequest.getUsername());
        result.put("roles", roles);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.register(registerRequest);
            Map<String, String> result = new HashMap<>();
            result.put("code", "200");
            result.put("msg", "注册成功");
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, String> result = new HashMap<>();
            result.put("code", "400");
            result.put("msg", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}