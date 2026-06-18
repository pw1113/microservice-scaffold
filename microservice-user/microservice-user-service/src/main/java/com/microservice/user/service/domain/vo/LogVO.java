package com.microservice.user.service.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogVO {
    private Long id;
    private Long adminUserId;
    private String adminUsername;        // 关联查询或后端填充
    private String operation;
    private String method;
    private String params;
    private String ip;
    private String requestId;
    private LocalDateTime createTime;    // 全局已配置格式
}
