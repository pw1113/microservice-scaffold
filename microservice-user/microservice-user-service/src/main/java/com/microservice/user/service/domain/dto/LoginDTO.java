// LoginDTO.java
package com.microservice.user.service.domain.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求参数
 */
@Data
public class LoginDTO {
    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 设备标识，多设备管理用 */
    private String deviceId;

    /** 邮箱 */
    @NotBlank(message = "邮箱不能为空")
    private String email;

    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String code;
}