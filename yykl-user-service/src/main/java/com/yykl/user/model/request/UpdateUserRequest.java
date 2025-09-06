
package com.yykl.user.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息更新请求对象")
public class UpdateUserRequest {
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Schema(description = "用户手机号", example = "13800138000")
    private String phone;

    @Schema(description = "头像URL地址", example = "https://example.com/avatar.jpg")
    private String avatar;
}