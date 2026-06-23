package com.microservice.user.service.domain.dto;

import com.microservice.user.service.enums.UserEnums;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新用户信息请求参数
 */
@Data
public class UserUpdateDTO {
    /** 用户ID */
    @NotNull(message = "用户ID不能为空")
    private Long id;

    /** 邮箱 */
    private String email;
    /** 手机号 */
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
    /** 个人描述 */
    private String description;
}
