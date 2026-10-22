package com.libiao.lblearn1.common.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 全局序列化配置。
 * <p>
 * JS 的 Number 安全整数上限为 2^53 - 1（约 16 位），而后端雪花 ID 是 19 位的 Long，
 * 直接以数字返回前端会丢失末几位精度（如 1234567890123456789 变成 1234567890123456800）。
 * 因此将 Long / long 统一序列化为字符串返回。
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer longToStringCustomizer() {
        return builder -> builder
                // Long：包装类型（实体/VO 中的 Long id 等）
                .serializerByType(Long.class, ToStringSerializer.instance)
                // long：基本类型
                .serializerByType(Long.TYPE, ToStringSerializer.instance);
    }
}
