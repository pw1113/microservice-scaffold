package com.microservice.user.service.domain.dto;

import com.microservice.user.service.enums.UserEnums;
import lombok.Data;

/**
 * 用户查询条件
 */
@Data
public class UserQueryDTO {
    private String username;
    private String email;
    private String phone;
    private UserEnums.RoleType roleType;
    private UserEnums.Status status;
}
