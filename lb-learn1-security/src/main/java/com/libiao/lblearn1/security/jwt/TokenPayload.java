package com.libiao.lblearn1.security.jwt;

import java.util.List;

/**
 * JWT 解析结果的中立载体
 * <p>
 * 对外只暴露业务需要的字段，避免 jjwt 的 {@code Claims} 类型泄漏到 security 模块之外，
 * 其他模块因此不必声明 jjwt 依赖。
 */
public record TokenPayload(Long userId, String username, List<String> roles, String tokenType, String jti,
        long expirationMillis) {
}
