package com.microservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑删除标记枚举
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum DeleteFlagEnum {

    /** 未删除 */
    NOT_DELETED(0, "未删除"),

    /** 已删除 */
    DELETED(1, "已删除");

    /** 标记编码 */
    private final Integer code;

    /** 标记描述 */
    private final String description;

}
