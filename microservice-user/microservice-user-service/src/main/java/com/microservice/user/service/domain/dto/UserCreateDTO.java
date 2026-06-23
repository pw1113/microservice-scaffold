package com.microservice.user.service.domain.dto;

import com.microservice.user.service.enums.UserEnums;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建用户请求参数
 */
@Data
public class UserCreateDTO {
    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 64)
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 128)
    private String password;

    /** 邮箱（注册时必填，用于发送验证码） */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /** 手机号 */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /** QQ号 */
    private String qq;
    /** 微信号 */
    private String wechat;
    /** 头像URL */
    private String avatar;
    /** 性别 */
    private UserEnums.Gender gender;
    /** 年龄 */
    private Integer age;
}
