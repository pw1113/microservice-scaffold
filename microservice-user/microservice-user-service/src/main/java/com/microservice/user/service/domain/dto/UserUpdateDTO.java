package com.microservice.user.service.domain.dto;

import com.microservice.user.service.enums.UserEnums;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long id;

    private String email;
    private String phone;
    private String qq;
    private String wechat;
    private String avatar;
    private UserEnums.Gender gender;
    private Integer age;
    private String description;
}
