// UserAuthDTO.java
package com.microservice.user.service.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserAuthDTO {
    private Long userId;
    private String username;
    private String email;
    private Integer roleType;
    private Integer status;
    // profile 相关
    private String lastLoginIp;
    private LocalDateTime lastLoginTime;
    private Integer onlineStatus;
    private Integer tokenVersion;
    private LocalDateTime pwdResetTime;
}