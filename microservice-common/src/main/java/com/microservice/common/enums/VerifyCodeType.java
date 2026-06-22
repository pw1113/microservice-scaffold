package com.microservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码类型枚举
 * <p>
 * 区分不同业务场景的验证码，用于生成不同的 Redis 缓存 Key，
 * 保证登录验证码和注册验证码互不干扰。
 * </p>
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum VerifyCodeType {

    /** 登录验证码 */
    LOGIN("LOGIN", "登录"),

    /** 注册验证码 */
    REGISTER("REGISTER", "注册");

    private final String code;
    private final String description;
}
