package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户刷新令牌持久化对象
 */
@Data
@Builder
@TableName("user_refresh_token")
public class UserRefreshTokenPO {
    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户ID */
    private Long userId;
    /** 设备标识 */
    private String deviceId;
    /** 刷新令牌 */
    private String refreshToken;
    /** 过期时间 */
    private LocalDateTime expireTime;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}