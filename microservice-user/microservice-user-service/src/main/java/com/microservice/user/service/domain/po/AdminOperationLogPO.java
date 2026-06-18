package com.microservice.user.service.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("admin_operation_log")
public class AdminOperationLogPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long adminUserId;
    private String operation;
    private String method;
    private String params;              // 请求参数，JSON或文本
    private String ip;
    private String requestId;
    private String traceId;
    private LocalDateTime createTime;
}