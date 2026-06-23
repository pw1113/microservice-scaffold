package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microservice.user.service.enums.UserEnums;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户档案持久化对象，存储登录信息和状态
 */
@Data
@TableName("user_profile")
public class UserProfilePO {
    /** 用户ID */
    @TableId
    private Long userId;
    /** 最后登录IP */
    private String lastLoginIp;
    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;
    /** 登录次数 */
    private Integer loginCount;
    /** 在线状态 */
    private UserEnums.OnlineStatus onlineStatus;
    /** token版本号，用于强制失效 */
    private Integer tokenVersion;
    /** 密码重置时间 */
    private LocalDateTime pwdResetTime;
}