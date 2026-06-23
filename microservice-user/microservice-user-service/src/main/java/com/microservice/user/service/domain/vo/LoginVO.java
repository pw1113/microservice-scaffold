package com.microservice.user.service.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录响应结果
 */
@Data
@AllArgsConstructor
public class LoginVO {
    /** 用户ID */
    private Long userId;
    /** 用户名 */
    private String username;
    /** 访问令牌（JWT） */
    private String accessToken;
    /** 刷新令牌（仅首次登录或刷新时返回） */
    private String refreshToken;
    /** access_token 过期秒数 */
    private long expiresIn;
}