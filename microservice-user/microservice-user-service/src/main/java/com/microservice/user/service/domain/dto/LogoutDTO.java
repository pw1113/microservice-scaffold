package com.microservice.user.service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登出请求参数
 */
@Data
public class LogoutDTO {

    /** 访问令牌 */
    @NotBlank(message = "accessToken不能为空")
    private String accessToken;

    /** 刷新令牌 */
    @NotBlank(message = "refreshToken不能为空")
    private String refreshToken;
}
