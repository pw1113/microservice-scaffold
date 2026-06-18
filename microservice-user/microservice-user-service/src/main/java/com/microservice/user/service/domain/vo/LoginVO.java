package com.microservice.user.service.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginVO {
    private Long userId;
    private String username;
    private String accessToken;          // JWT
    private String refreshToken;         // 仅首次登录或刷新时返回
    private long expiresIn;              // access_token 过期秒数
}