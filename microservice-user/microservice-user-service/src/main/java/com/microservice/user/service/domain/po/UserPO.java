package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Integer gender;             // 1-男, 2-女
    private Integer age;
    private String description;         // JSON 字符串，数据库 JSON 类型映射
    private Integer roleType;           // 1-管理员, 2-普通用户, 3-游客
    private Integer status;             // 1-正常, 2-冻结
    private Integer deleted;            // 0-未删除, 1-已删除
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}