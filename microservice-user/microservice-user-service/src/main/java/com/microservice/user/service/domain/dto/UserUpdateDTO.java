// UserUpdateDTO.java
package com.microservice.user.service.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long id;

    // 以下字段可选更新
    private String email;
    private String phone;
    private String qq;
    private String wechat;
    private String avatar;
    private Integer gender;
    private Integer age;
    private String description;         // 更新时覆盖JSON
    // 注意：username 和 password 不在更新范围（密码单独修改）
}