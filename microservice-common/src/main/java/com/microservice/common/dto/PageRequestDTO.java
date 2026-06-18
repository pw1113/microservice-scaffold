package com.microservice.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页请求参数基类
 * @param <T> 模糊查询条件类
 */
@Data
public class PageRequestDTO<T> {
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页最少1条")
    @Max(value = 100, message = "每页最多100条")
    private Integer pageSize = 20;

    /**
     * 模糊查询条件
     */
    private T query;
}
