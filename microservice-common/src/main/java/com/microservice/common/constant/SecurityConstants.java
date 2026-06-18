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

    /** 验证码 Key 前缀 */
    public static final String REDIS_KEY_VERIFY_CODE = "auth:verify_code:";

    /** Token 黑名单 Key 前缀 */
    public static final String REDIS_KEY_TOKEN_BLACKLIST = "auth:token_blacklist:";

    /** RefreshToken 黑名单 Key 前缀 */
    public static final String REDIS_KEY_REFRESH_BLACKLIST = "auth:refresh_blacklist:";

    /** Token 版本号 Key 前缀 */
    public static final String REDIS_KEY_TOKEN_VERSION = "auth:token_version:";

    // ======================== 白名单路径 ========================

    /** 无需认证的路径 */
    public static final String[] WHITE_LIST = {
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/auth/send-code",
            "/doc.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/favicon.ico"
    };

}
