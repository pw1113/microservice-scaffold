package com.microservice.user.service.domain.vo;

import com.microservice.user.service.enums.UserEnums;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String qq;
    private String wechat;
    private String avatar;
    private UserEnums.Gender gender;
    private Integer age;
    private String description;
    private UserEnums.RoleType roleType;
    private UserEnums.Status status;
    private LocalDateTime createTime;
    private String lastLoginIp;
    private LocalDateTime lastLoginTime;
    private Integer loginCount;
    private UserEnums.OnlineStatus onlineStatus;
}
