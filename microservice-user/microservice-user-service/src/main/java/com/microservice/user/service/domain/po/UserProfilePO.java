package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Integer onlineStatus;       // 0-离线, 1-在线
    private Integer tokenVersion;
    private LocalDateTime pwdResetTime;
}