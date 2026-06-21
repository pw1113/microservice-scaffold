package com.microservice.common.config;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * 枚举 Swagger 文档转换器
 * <p>
 * 对带有 {@code @JsonValue} 的枚举：
 * <ul>
 *   <li>把 schema 类型映射为 {@code integer}/{@code string}（而不是默认的字符串 + 枚举名）；</li>
 *   <li>通过 {@link Schema#addEnumItem} 注入所有 code 值，驱动 Swagger UI 的 "Available values"；</li>
 *   <li>透传枚举类上的 {@code @Schema(description=...)} 到 schema description（不做任何加工）。</li>
 * </ul>
 * </p>
 * <p>
 * <b>关于字段层 description：</b>曾尝试在 ModelConverter / PropertyCustomizer 里拼接
 * {@code "性别：0-未知，1-男，2-女"} 自动覆盖字段 description，但 springdoc 在生成 DTO 属性时
 * 会直接读取枚举类上的 {@code @Schema(description=...)} 作为字段描述，时序上覆盖不掉。
 * 因此放弃自动拼接，code 映射直接由使用者写在 {@code @Schema(description=...)} 字符串里，例如：
 * <pre>
 *   &#64;Schema(description = "性别：0-未知，1-男，2-女")
 *   public enum Gender { ... }
 * </pre>
 * </p>
 *
 * @author microservice
 */
@Configuration
@ConditionalOnClass(name = "io.swagger.v3.core.converter.ModelConverter")
public class EnumModelConverterConfig {

    private static final Logger log = LoggerFactory.getLogger(EnumModelConverterConfig.class);

    @PostConstruct
    public void register() {
        ModelConverters.getInstance().addConverter(new EnumModelConverter());
        log.info("EnumModelConverter registered into ModelConverters chain");
    }

    static class EnumModelConverter implements ModelConverter {

        @Override
        public Schema<?> resolve(AnnotatedType annotatedType,
                                 ModelConverterContext context,
                                 Iterator<ModelConverter> chain) {
            Type type = annotatedType.getType();
            if (type == null) {
                return chain.hasNext() ? chain.next().resolve(annotatedType, context, chain) : null;
            }

            Class<?> rawClass = extractRawClass(type);
            if (rawClass == null || !rawClass.isEnum()) {
                return chain.hasNext() ? chain.next().resolve(annotatedType, context, chain) : null;
            }

            Field codeField = findJsonValueField(rawClass);
            if (codeField == null) {
                return chain.hasNext() ? chain.next().resolve(annotatedType, context, chain) : null;
            }

            return buildEnumSchema(rawClass, codeField);
        }

        private Class<?> extractRawClass(Type type) {
            if (type instanceof Class<?>) {
                return (Class<?>) type;
            }
            if (type instanceof ParameterizedType pt) {
                Type raw = pt.getRawType();
                if (raw instanceof Class<?>) {
                    return (Class<?>) raw;
                }
            }
            return null;
        }

        private Field findJsonValueField(Class<?> enumClass) {
            for (Field field : enumClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(JsonValue.class)) {
                    return field;
                }
            }
            for (Method method : enumClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(JsonValue.class)) {
                    String fieldName = method.getName()
                            .replaceFirst("get", "")
                            .replaceFirst("is", "");
                    if (fieldName.isEmpty()) {
                        continue;
                    }
                    fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
                    try {
                        return enumClass.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException ignored) {
                    }
                }
            }
            return null;
        }

        private Schema<?> buildEnumSchema(Class<?> enumClass, Field codeField) {
            Class<?> codeType = codeField.getType();
            Object[] constants = enumClass.getEnumConstants();
            if (constants.length == 0) {
                return new Schema<>();
            }

            String classDesc = readClassSchemaDescription(enumClass);

            if (codeType == Integer.class || codeType == int.class) {
                IntegerSchema schema = new IntegerSchema();
                schema.format("int32");
                for (Object constant : constants) {
                    Integer code = getIntegerCodeValue(constant, codeField);
                    if (code != null) {
                        schema.addEnumItem(code);
                    }
                }
                if (classDesc != null) {
                    schema.setDescription(classDesc);
                }
                return schema;
            } else if (codeType == String.class) {
                StringSchema schema = new StringSchema();
                for (Object constant : constants) {
                    String code = getStringCodeValue(constant, codeField);
                    if (code != null) {
                        schema.addEnumItem(code);
                    }
                }
                if (classDesc != null) {
                    schema.setDescription(classDesc);
                }
                return schema;
            }

            return new Schema<>();
        }

        /**
         * 透传枚举类上的 {@code @Schema(description=...)}；无注解或空串返回 null
         */
        private String readClassSchemaDescription(Class<?> enumClass) {
            io.swagger.v3.oas.annotations.media.Schema annotation =
                    enumClass.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
            if (annotation == null) {
                return null;
            }
            String desc = annotation.description();
            return (desc == null || desc.isEmpty()) ? null : desc;
        }

        private Integer getIntegerCodeValue(Object enumConst, Field codeField) {
            try {
                codeField.setAccessible(true);
                Object val = codeField.get(enumConst);
                return val instanceof Integer ? (Integer) val : null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        private String getStringCodeValue(Object enumConst, Field codeField) {
            try {
                codeField.setAccessible(true);
                Object val = codeField.get(enumConst);
                return val instanceof String ? (String) val : null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
    }

}
