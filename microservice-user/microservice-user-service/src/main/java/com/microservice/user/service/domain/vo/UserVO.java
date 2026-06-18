package com.microservice.user.service.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String qq;
    private String wechat;
    private String avatar;
    private Integer gender;              // 1-男, 2-女
    private Integer age;

    // 将数据库中 JSON 字符串原样返回，前端自行解析；也可改为 Object 并自定义转换
    private String description;

    // 枚举转义
    private String roleTypeDesc;         // 管理员/普通用户/游客
    private String statusDesc;           // 正常/冻结
    private String genderDesc;           // 男/女/未知

    private LocalDateTime createTime;

    // profile 关联字段
    private String lastLoginIp;
    private LocalDateTime lastLoginTime;
    private Integer loginCount;
    private Integer onlineStatus;        // 0-离线, 1-在线
}
