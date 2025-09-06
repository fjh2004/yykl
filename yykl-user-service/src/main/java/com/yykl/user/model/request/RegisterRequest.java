// RegisterRequest.java
package com.yykl.user.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "用户注册请求对象")
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "登录账号，将用于登录系统", required = true, example = "zhangsan")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "登录密码，用于系统登录验证", required = true, example = "123456")
    private String password;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "用户手机号，需符合手机号格式规范", required = true, example = "13800138000")
    private String phone;

    @Schema(description = "用户昵称",  example = "张三")
    private String nickname;
}