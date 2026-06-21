package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microservice.user.service.enums.UserEnums;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class UserPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;            // 密文，不对外暴露
    private String email;
    private String phone;
    private String qq;
    private String wechat;
    private String avatar;
    private UserEnums.Gender gender;
    private Integer age;
    private String description;         // JSON 字符串，数据库 JSON 类型映射
    private UserEnums.RoleType roleType;
    private UserEnums.Status status;
    private UserEnums.Deleted deleted = UserEnums.Deleted.NOT_DELETED;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
