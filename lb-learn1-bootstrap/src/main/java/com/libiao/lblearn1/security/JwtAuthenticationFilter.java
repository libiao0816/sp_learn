package com.libiao.lblearn1.security;

import com.libiao.lblearn1.common.enums.TokenTypeEnum;
import com.libiao.lblearn1.common.properties.AuthProperties;
import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.domain.security.LoginSysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";

    private static final String PREFIX = "Bearer ";

    /** 认证失败原因：accessToken 已过期 */
    public static final String REASON_EXPIRED = "EXPIRED";

    /** 认证失败原因存放在 request 中的属性名，供 AuthenticationEntryPointImpl 读取 */
    public static final String AUTH_FAIL_REASON_ATTR = "AUTH_FAIL_REASON";

    private final JwtProperties jwtProperties;

    private final TokenBlackService tokenBlackService;

    private final AuthProperties authProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        // 关键判断：只有「拿到 token」且「当前还没认证过」才处理
        // 第二个条件能避免覆盖前面过滤器已经放好的身份
        if (StringUtils.isNotBlank(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Claims claims = JwtUtil.parseToken(token, jwtProperties.getSecret());// 签名不对/过期 → 抛异常

                List<String> whiteList = authProperties.getWhiteList();
                // 如果是 refresh token，且当前请求不是 /auth/refresh → 拒绝
                String tokenType = claims.get("tokenType", String.class);
                if (TokenTypeEnum.REFRESH_TOKEN.getType().equals(tokenType)
                        && !whiteList.contains(request.getRequestURI())) {
                    filterChain.doFilter(request, response); // 走 401 return ;
                    return;
                }

                // 注销黑名单校验：命中就当没登录，交给入口点返回 401
                if (tokenBlackService.isBlackList(claims.getId())) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Long sysUserId = Long.valueOf(claims.getSubject());
                String username = claims.get("username", String.class);

                @SuppressWarnings("unchecked")
                List<String> authoritiesStr = claims.get("roles", List.class);
                // JWT 里存的 authority 已经带好前缀（ROLE_xxx 或 PERMISSION_xxx），直接使用即可
                List<SimpleGrantedAuthority> authorities = authoritiesStr == null ? List.of()
                        : authoritiesStr.stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList();

                LoginSysUser principal = new LoginSysUser(sysUserId, username, List.copyOf(authorities));

                // ★ Security 6 推荐写法：新建空 Context 再 set，避免复用线程上的残留身份
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(
                        new UsernamePasswordAuthenticationToken(principal, null, authorities));
                SecurityContextHolder.setContext(context);

            } catch (ExpiredJwtException e) {
                // ★ accessToken 已过期：标记失败原因，交给 AuthenticationEntryPointImpl 返回 4011，
                // 前端据此用 refreshToken 换新 token 并重发原请求
                request.setAttribute(AUTH_FAIL_REASON_ATTR, REASON_EXPIRED);
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                // 其他解析失败（篡改/签名不符/格式错）：清空上下文，不在这里返回错误，
                // 让后面的授权规则判定为"未认证" → ExceptionTranslationFilter 走 401
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response); // ★ 无论如何都要放行，交给链上后续过滤器
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIX)) {
            return header.substring(PREFIX.length());
        }
        return null;
    }
}
