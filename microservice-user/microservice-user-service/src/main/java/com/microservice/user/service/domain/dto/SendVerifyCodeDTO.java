package com.microservice.user.service.domain.dto;

import com.microservice.common.enums.VerifyCodeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发送验证码请求参数
 */
@Data
public class SendVerifyCodeDTO {

    /** 目标邮箱 */
    @Email(message = "邮箱格式不正确")
    @NotNull(message = "邮箱不能为空")
    private String email;

    /** 验证码类型：LOGIN-登录，REGISTER-注册 */
    @NotNull(message = "验证码类型不能为空")
    private VerifyCodeType type;
}
