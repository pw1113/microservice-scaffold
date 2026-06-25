package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microservice.user.service.enums.UserEnums;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户持久化对象
 */
@Data
@Builder
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
    /** 手机号（可选） */
    private String phone;
    /** QQ号（可选） */
    private String qq;
    /** 微信号（可选） */
    private String wechat;
    /** 头像URL（默认头像） */
    private String avatar = "https://upload-bbs.miyoushe.com/upload/2022/07/03/252664962/acf379fcba31954ad36f38688bfeab85_6753007254972183423.jpg";
    /** 性别（默认未知） */
    private UserEnums.Gender gender = UserEnums.Gender.UNKNOWN;
    /** 年龄（可选） */
    private Integer age;
    /** 个人描述（可选） */
    private String description;
    /** 角色类型（默认普通用户） */
    private UserEnums.RoleType roleType = UserEnums.RoleType.USER;
    /** 账号状态（默认正常） */
    private UserEnums.Status status = UserEnums.Status.NORMAL;
    /** 删除标志（默认未删除） */
    private UserEnums.Deleted deleted = UserEnums.Deleted.NOT_DELETED;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
