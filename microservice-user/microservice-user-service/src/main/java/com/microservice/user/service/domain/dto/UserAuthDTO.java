package com.microservice.user.service.domain.dto;

import com.microservice.user.service.enums.UserEnums;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户认证信息DTO，用于鉴权和Token相关操作
 */
@Data
public class UserAuthDTO {
    /** 用户ID */
    private Long userId;
    /** 用户名 */
    private String username;
    /** 邮箱 */
    private String email;
    /** 角色类型 */
    private UserEnums.RoleType roleType;
    /** 账号状态 */
    private UserEnums.Status status;
    /** 最后登录IP */
    private String lastLoginIp;
    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;
    /** 在线状态 */
    private UserEnums.OnlineStatus onlineStatus;
    /** token版本号，用于强制失效 */
    private Integer tokenVersion;
    /** 密码重置时间 */
    private LocalDateTime pwdResetTime;
}
