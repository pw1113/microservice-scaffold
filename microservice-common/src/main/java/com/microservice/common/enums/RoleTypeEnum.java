package com.microservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色类型枚举
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    /** 管理员 */
    ADMIN(1, "管理员"),

    /** 普通用户 */
    USER(2, "普通用户"),

    /** 游客 */
    GUEST(3, "游客");

    /** 角色编码 */
    private final Integer code;

    /** 角色描述 */
    private final String description;

    /**
     * 根据 code 获取枚举
     */
    public static RoleTypeEnum of(Integer code) {
        if (code == null) {
            return GUEST;
        }
        for (RoleTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return GUEST;
    }

}
