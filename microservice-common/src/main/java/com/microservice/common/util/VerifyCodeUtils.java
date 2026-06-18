package com.microservice.common.util;

import java.security.SecureRandom;

/**
 * 验证码生成工具类
 *
 * @author microservice
 */
public final class VerifyCodeUtils {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 6;

    private VerifyCodeUtils() {
    }

    /**
     * 生成6位纯数字验证码
     *
     * @return 6位数字验证码字符串
     */
    public static String generateCode() {
        return generateCode(DEFAULT_LENGTH);
    }

    /**
     * 生成指定长度的纯数字验证码
     *
     * @param length 验证码长度
     * @return 数字验证码字符串
     */
    public static String generateCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("验证码长度必须大于0");
        }
        int bound = (int) Math.pow(10, length);
        int code = RANDOM.nextInt(bound);
        return String.format("%0" + length + "d", code);
    }
}
