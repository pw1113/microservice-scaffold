package com.microservice.user.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户模块统一枚举
 * <p>
 * 集中管理用户相关所有业务枚举，包括性别、角色、状态、删除标记、在线状态。
 * </p>
 * <p>
 * {@code @EnumValue} — MyBatis-Plus 读写数据库时使用 code 字段
 * {@code @JsonValue}  — Jackson 序列化 JSON 时输出 code 值
 * {@code @JsonCreator} — Jackson 反序列化 JSON 时根据 code 值还原枚举
 * </p>
 * <p>
 * Swagger 文档由 {@code EnumModelConverterConfig} 自动处理，
 * 根据 {@code @JsonValue} 字段和 {@code description} 字段生成可选值说明。
 * </p>
 *
 * @author microservice
 */
public final class UserEnums {

    private UserEnums() {
    }

    // ==================== 性别 ====================

    @Getter
    @AllArgsConstructor
    @Schema(description = "性别：0-未知，1-男，2-女")
    public enum Gender {

        UNKNOWN(0, "未知"),
        MALE(1, "男"),
        FEMALE(2, "女");

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String description;

        @JsonCreator
        public static Gender of(Integer code) {
            if (code == null) {
                return UNKNOWN;
            }
            for (Gender g : values()) {
                if (g.code.equals(code)) {
                    return g;
                }
            }
            return UNKNOWN;
        }
    }

    // ==================== 角色类型 ====================

    @Getter
    @AllArgsConstructor
    @Schema(description = "角色类型：1-管理员，2-普通用户，3-游客")
    public enum RoleType {

        ADMIN(1, "管理员"),
        USER(2, "普通用户"),
        GUEST(3, "游客");

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String description;

        @JsonCreator
        public static RoleType of(Integer code) {
            if (code == null) {
                return GUEST;
            }
            for (RoleType r : values()) {
                if (r.code.equals(code)) {
                    return r;
                }
            }
            return GUEST;
        }
    }

    // ==================== 用户状态 ====================

    @Getter
    @AllArgsConstructor
    @Schema(description = "用户状态：1-正常，2-冻结")
    public enum Status {

        NORMAL(1, "正常"),
        FROZEN(2, "冻结");

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String description;

        @JsonCreator
        public static Status of(Integer code) {
            if (code == null) {
                return NORMAL;
            }
            for (Status s : values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return NORMAL;
        }
    }

    // ==================== 逻辑删除标记 ====================

    @Getter
    @AllArgsConstructor
    @Schema(description = "逻辑删除标记：0-未删除，1-已删除")
    public enum Deleted {

        NOT_DELETED(0, "未删除"),
        DELETED(1, "已删除");

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String description;

        @JsonCreator
        public static Deleted of(Integer code) {
            if (code == null) {
                return NOT_DELETED;
            }
            for (Deleted d : values()) {
                if (d.code.equals(code)) {
                    return d;
                }
            }
            return NOT_DELETED;
        }
    }

    // ==================== 在线状态 ====================

    @Getter
    @AllArgsConstructor
    @Schema(description = "在线状态：0-离线，1-在线")
    public enum OnlineStatus {

        OFFLINE(0, "离线"),
        ONLINE(1, "在线");

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String description;

        @JsonCreator
        public static OnlineStatus of(Integer code) {
            if (code == null) {
                return OFFLINE;
            }
            for (OnlineStatus o : values()) {
                if (o.code.equals(code)) {
                    return o;
                }
            }
            return OFFLINE;
        }
    }

}
