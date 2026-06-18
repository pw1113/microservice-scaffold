package com.microservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    /** 正常 */
    NORMAL(1, "正常"),

    /** 冻结 */
    FROZEN(2, "冻结");

    /** 状态编码 */
    private final Integer code;

    /** 状态描述 */
    private final String description;

}
