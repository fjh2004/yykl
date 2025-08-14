
package com.yykl.user.model.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String nickname;
    private String phone;
    private String avatar; // 头像URL
}