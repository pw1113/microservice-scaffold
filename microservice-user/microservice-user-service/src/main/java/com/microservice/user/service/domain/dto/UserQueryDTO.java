package com.microservice.user.service.domain.dto;

import com.microservice.user.service.enums.UserEnums;
import lombok.Data;

/**
 * 用户查询条件
 */
@Data
public class UserQueryDTO {
    /** 用户名 */
    private String username;
    /** 邮箱 */
    private String email;
    /** 手机号 */
    private String phone;
    /** 角色类型 */
    private UserEnums.RoleType roleType;
    /** 账号状态 */
    private UserEnums.Status status;
}
