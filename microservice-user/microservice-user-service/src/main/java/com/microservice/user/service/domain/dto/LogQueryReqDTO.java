// LogQueryReqDTO.java
package com.microservice.user.service.domain.dto;

import com.microservice.common.dto.PageRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
public class LogQueryReqDTO  {
    private Long adminUserId;
    private String operation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}