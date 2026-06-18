package com.microservice.common.context;

/**
 * 用户上下文（基于 ThreadLocal 存储当前请求的用户信息）
 * <p>
 * 由 UserContextInterceptor 从网关透传的 Header 中解析并设置，
 * 请求结束后自动清理。
 * </p>
 *
 * @author microservice
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_NAME = new ThreadLocal<>();
    private static final ThreadLocal<Integer> ROLE_TYPE = new ThreadLocal<>();

    private UserContext() {
    }

    // ======================== Setter ========================

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static void setUserName(String userName) {
        USER_NAME.set(userName);
    }

    public static void setRoleType(Integer roleType) {
        ROLE_TYPE.set(roleType);
    }

    // ======================== Getter ========================

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static String getUserName() {
        return USER_NAME.get();
    }

    public static Integer getRoleType() {
        return ROLE_TYPE.get();
    }

    // ======================== Remove ========================

    public static void clear() {
        USER_ID.remove();
        USER_NAME.remove();
        ROLE_TYPE.remove();
    }

}
