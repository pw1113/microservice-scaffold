package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microservice.user.service.enums.UserEnums;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户持久化对象
 */
@Data
@TableName("user")
public class UserPO {
    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户名 */
    private String username;
    /** 密码（密文，不对外暴露） */
    private String password;
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
    /** 个人描述（JSON字符串） */
    private String description;
    /** 角色类型 */
    private UserEnums.RoleType roleType;
    /** 账号状态 */
    private UserEnums.Status status;
    /** 删除标志 */
    private UserEnums.Deleted deleted = UserEnums.Deleted.NOT_DELETED;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
