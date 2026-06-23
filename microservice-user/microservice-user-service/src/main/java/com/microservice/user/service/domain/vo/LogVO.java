package com.microservice.user.service.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志视图对象
 */
@Data
public class LogVO {
    /** 主键ID */
    private Long id;
    /** 管理员用户ID */
    private Long adminUserId;
    /** 管理员用户名（关联查询或后端填充） */
    private String adminUsername;
    /** 操作描述 */
    private String operation;
    /** 请求方法 */
    private String method;
    /** 请求参数 */
    private String params;
    /** 请求IP */
    private String ip;
    /** 请求ID */
    private String requestId;
    /** 创建时间 */
    private LocalDateTime createTime;
}
