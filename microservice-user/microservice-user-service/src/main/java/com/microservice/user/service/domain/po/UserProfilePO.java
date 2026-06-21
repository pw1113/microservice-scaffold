package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microservice.user.service.enums.UserEnums;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_profile")
public class UserProfilePO {
    @TableId
    private Long userId;
    private String lastLoginIp;
    private LocalDateTime lastLoginTime;
    private Integer loginCount;
    private UserEnums.OnlineStatus onlineStatus;
    private Integer tokenVersion;
    private LocalDateTime pwdResetTime;
}