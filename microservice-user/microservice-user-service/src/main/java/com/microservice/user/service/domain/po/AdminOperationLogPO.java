package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 管理员操作日志持久化对象
 */
@Data
@TableName("admin_operation_log")
public class AdminOperationLogPO {
    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 管理员用户ID */
    private Long adminUserId;
    /** 操作描述 */
    private String operation;
    /** 请求方法 */
    private String method;
    /** 请求参数，JSON或文本 */
    private String params;
    /** 请求IP */
    private String ip;
    /** 请求ID */
    private String requestId;
    /** 链路追踪ID */
    private String traceId;
    /** 创建时间 */
    private LocalDateTime createTime;
}