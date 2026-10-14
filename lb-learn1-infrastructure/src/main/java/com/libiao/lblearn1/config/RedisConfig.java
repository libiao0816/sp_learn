package com.libiao.lblearn1.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 自定义 RedisTemplate 序列化策略:
     * - Key / HashKey: StringRedisSerializer (保证客户端可读, 避免乱码)
     * - Value / HashValue: GenericJackson2JsonRedisSerializer (对象存成 JSON, 保留类型信息)
     *
     * 相比无参构造, 这里显式注入 ObjectMapper, 目的有两个:
     * 1. 注册 JavaTimeModule, 使 LocalDateTime 等时间类型能正常序列化
     * 2. 通过 BasicPolymorphicTypeValidator 限定反序列化允许的类型白名单, 防止反序列化任意类引发安全问题
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        ObjectMapper objectMapper = new ObjectMapper();
        // 支持 Java 时间类型 (LocalDateTime 等)
        objectMapper.registerModule(new JavaTimeModule());
        // 时间序列化为可读字符串而不是时间戳数字
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 安全限制: 只允许反序列化项目包及常用 JDK 类型, 避免反序列化任意类
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.libiao.lblearn1.")
                .allowIfSubType("java.util.")
                .allowIfSubType("java.lang.")
                .allowIfSubType("java.time.")
                .build();

        objectMapper.activateDefaultTyping(
                ptv,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        // Key 使用字符串序列化, Value 使用 JSON 序列化
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);

        // Hash 的 field 用字符串, value 用 JSON
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}