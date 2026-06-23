package com.microservice.user.service.domain.vo;

import com.microservice.user.service.enums.UserEnums;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户视图对象
 */
@Data
public class UserVO {
    /** 用户ID */
    private Long id;
    /** 用户名 */
    private String username;
    /** 邮箱 */
    private String email;
    /** 手机号 */
    private String phone;
    /** QQ号 */
    private String qq;
    /** 微信号 */
    private String wechat;
    /** 头像URL */
    private String avatar;
    /** 性别 */
    private UserEnums.Gender gender;
    /** 年龄 */
    private Integer age;
    /** 个人描述 */
    private String description;
    /** 角色类型 */
    private UserEnums.RoleType roleType;
    /** 账号状态 */
    private UserEnums.Status status;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后登录IP */
    private String lastLoginIp;
    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;
    /** 登录次数 */
    private Integer loginCount;
    /** 在线状态 */
    private UserEnums.OnlineStatus onlineStatus;
}
