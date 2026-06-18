package com.microservice.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密工具类（BCrypt 密码加密）
 *
 * @author microservice
 */
public class EncryptUtils {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private EncryptUtils() {
    }

    /**
     * 对明文密码进行 BCrypt 加密
     *
     * @param rawPassword 明文密码
     * @return 加密后的密文
     */
    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /**
     * 校验明文密码与密文是否匹配
     *
     * @param rawPassword     明文密码
     * @param encodedPassword 密文
     * @return true-匹配, false-不匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }

}
