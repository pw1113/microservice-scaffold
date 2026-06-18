package com.microservice.user.service.domain.dto;

import lombok.Data;

/**
 * 用户查询条件
 */
@Data
public class UserQueryDTO {
    private String username;
    private String email;
    private String phone;
    private Integer roleType;
    private Integer status;
}
