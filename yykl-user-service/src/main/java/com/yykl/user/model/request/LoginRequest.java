// LoginRequest.java
package com.yykl.user.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "用户登录请求对象")
public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "登录账号", required = true, example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "登录密码", required = true, example = "123456")
    private String password;
}