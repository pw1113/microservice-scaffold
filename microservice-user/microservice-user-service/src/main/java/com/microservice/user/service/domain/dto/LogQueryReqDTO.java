// LogQueryReqDTO.java
package com.microservice.user.service.domain.dto;

import com.microservice.common.dto.PageRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 操作日志查询请求参数
 */
@Data
public class LogQueryReqDTO  {
    /** 管理员用户ID */
    private Long adminUserId;
    /** 操作类型 */
    private String operation;
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;
}