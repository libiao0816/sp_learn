package com.libiao.lblearn1.common.utils;

import com.libiao.lblearn1.common.enums.TokenTypeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.time.Duration;
import java.util.UUID;

import javax.crypto.SecretKey;

public class JwtUtil {

    // HS256 要求密钥 >= 256 位（即 32 字节），太短启动/签名时直接抛 WeakKeyException
    // 生产环境放配置中心/环境变量，且要足够随机，绝不写死提交到 git
    // 1. SECRET：签名密钥的原始字符串，是 JWT 防篡改的"密码本"，只有服务端知道
    // 服务端用它给 token 签名，也用它验证 token 是否被伪造/篡改
    //private static final String SECRET = jwtProperties.getSecret();

    // 2. KEY：把字符串密钥转换成 JJWT 库要求的 SecretKey 对象（HMAC-SHA 算法专用密钥）
    // 类加载时初始化一次，后续生成和解析 token 都直接复用，避免每次重复构造
    //private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // 3. EXPIRE：token 的有效期，签发时以此计算过期时间（当前时间 + 1 小时）
    // 过期后 token 失效需重新登录；实际项目一般 30 分钟 ~ 2 小时
    //    private static final Duration EXPIRE = Duration.ofMillis(jwtProperties.getTtl());

    public static String subStrToken(String token) {
        return token.substring(7);
    }

    public static SecretKey getKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 签发访问令牌：短有效期，用于每次业务请求的鉴权
     */
    public static String createAccessToken(Long userId, String username, String secret, Long ttl, List<String> userRoles) {
        return buildToken(userId, username, secret, ttl, userRoles, TokenTypeEnum.ACCESS_TOKEN.getType());
    }

    /**
     * 签发刷新令牌：长有效期，只用于换取新的访问令牌
     */
    public static String createRefreshToken(Long userId, String username, String secret, Long ttl, List<String> userRoles) {
        return buildToken(userId, username, secret, ttl, userRoles, TokenTypeEnum.REFRESH_TOKEN.getType());
    }

    private static String buildToken(Long userId, String username, String secret, Long ttl, List<String> userRoles, String type) {
        return Jwts.builder().subject(userId.toString())
                .id(UUID.randomUUID().toString())
                .claim("username", username)
                .claim("roles", userRoles)
                .claim("tokenType", type)
                .issuedAt(new Date()) // 签发时间
                .expiration(new Date(System.currentTimeMillis() + ttl)) // 过期时间
                .signWith(getKey(secret))
                .compact();
    }

    public static Claims parseToken(String token,String secret) {
        String bearerToken = token.replace("Bearer ", "");
        return Jwts.parser()
                .verifyWith(getKey(secret))
                .build()
                .parseSignedClaims(bearerToken)
                .getPayload();
    }

}
