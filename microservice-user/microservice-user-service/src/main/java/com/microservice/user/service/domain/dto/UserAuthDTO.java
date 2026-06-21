package com.microservice.user.service.domain.dto;

import com.microservice.user.service.enums.UserEnums;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserAuthDTO {
    private Long userId;
    private String username;
    private String email;
    private UserEnums.RoleType roleType;
    private UserEnums.Status status;
    private String lastLoginIp;
    private LocalDateTime lastLoginTime;
    private UserEnums.OnlineStatus onlineStatus;
    private Integer tokenVersion;
    private LocalDateTime pwdResetTime;
}
