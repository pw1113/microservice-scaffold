package com.microservice.common.constant;

/**
 * 安全相关常量
 *
 * @author microservice
 */
public class SecurityConstants {

    private SecurityConstants() {
    }

    // ======================== HTTP Header 透传字段 ========================

    /** 用户 ID Header */
    public static final String HEADER_USER_ID = "X-User-Id";

    /** 用户名 Header */
    public static final String HEADER_USER_NAME = "X-User-Name";

    /** 用户角色 Header */
    public static final String HEADER_USER_ROLE = "X-User-Role";

    /** 链路追踪 ID Header */
    public static final String HEADER_TRACE_ID = "X-Trace-Id";

    // ======================== JWT Claims ========================

    /** JWT Claim: 用户 ID */
    public static final String CLAIM_USER_ID = "userId";

    /** JWT Claim: 用户名 */
    public static final String CLAIM_USERNAME = "username";

    /** JWT Claim: 角色类型 */
    public static final String CLAIM_ROLE_TYPE = "roleType";

    /** JWT Claim: Token 版本号 */
    public static final String CLAIM_TOKEN_VERSION = "tokenVersion";

    // ======================== Redis Key 前缀 ========================

    /** 验证码 Key 前缀（旧，逐步废弃） */
    public static final String REDIS_KEY_VERIFY_CODE = "auth:verify_code:";

    /** 登录验证码 Key 前缀 */
    public static final String REDIS_KEY_LOGIN_VERIFY_CODE = "auth:login_verify_code:";

    /** 注册验证码 Key 前缀 */
    public static final String REDIS_KEY_REGISTER_VERIFY_CODE = "auth:register_verify_code:";

    /** Token 黑名单 Key 前缀 */
    public static final String REDIS_KEY_TOKEN_BLACKLIST = "auth:token_blacklist:";

    /** RefreshToken 黑名单 Key 前缀 */
    public static final String REDIS_KEY_REFRESH_BLACKLIST = "auth:refresh_blacklist:";

    /** Token 版本号 Key 前缀 */
    public static final String REDIS_KEY_TOKEN_VERSION = "auth:token_version:";

    /** 登录失败计数 Key 前缀 */
    public static final String REDIS_KEY_LOGIN_FAIL_COUNT = "auth:login_fail:";

    // ======================== 登录限制配置 ========================

    /** 最大登录失败次数 */
    public static final int LOGIN_FAIL_MAX_COUNT = 5;

    /** 登录失败锁定时间（秒） */
    public static final int LOGIN_FAIL_LOCK_SECONDS = 15 * 60;

    // ======================== 白名单路径 ========================

    /** 无需认证的路径 */
    public static final String[] WHITE_LIST = {
            "/user/login",
            "/user/register",
            "/user/refresh",
            "/user/send-code",
            "/doc.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/favicon.ico"
    };

}
