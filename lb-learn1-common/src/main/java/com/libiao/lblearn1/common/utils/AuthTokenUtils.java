package com.libiao.lblearn1.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证过滤器共用的 token 提取逻辑与认证失败标记约定。
 * <p>
 * 由 security 模块的认证过滤器使用，失败原因常量则由 AuthenticationEntryPointImpl 读取。
 */
public final class AuthTokenUtils {

    /** 承载 token 的请求头名 */
    public static final String HEADER = "Authorization";

    /** token 前缀 */
    public static final String PREFIX = "Bearer ";

    /** 认证失败原因：accessToken 已过期 */
    public static final String REASON_EXPIRED = "EXPIRED";

    /** 认证失败原因存放在 request 中的属性名，供 AuthenticationEntryPointImpl 读取 */
    public static final String AUTH_FAIL_REASON_ATTR = "AUTH_FAIL_REASON";

    private AuthTokenUtils() {
    }

    /**
     * 从 Authorization 请求头中取出 Bearer token，取不到返回 null
     */
    public static String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIX)) {
            return header.substring(PREFIX.length());
        }
        return null;
    }
}
