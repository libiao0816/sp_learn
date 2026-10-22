package com.libiao.lblearn1.security.filter;

import com.libiao.lblearn1.common.properties.AuthProperties;
import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.common.model.TokenPayload;
import com.libiao.lblearn1.common.utils.AuthTokenUtils;
import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.redis.TokenBlackService;
import com.libiao.lblearn1.common.enums.TokenTypeEnum;
import com.libiao.lblearn1.security.sysUser.LoginSysUser;

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

    private final JwtProperties jwtProperties;

    private final TokenBlackService tokenBlackService;

    private final AuthProperties authProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = AuthTokenUtils.resolveToken(request);

        // 关键判断：只有「拿到 token」且「当前还没认证过」才处理
        // 第二个条件能避免覆盖前面过滤器已经放好的身份
        if (StringUtils.isNotBlank(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                TokenPayload payload = JwtUtil.parseToken(token, jwtProperties.getAdmin().getSecret());// 签名不对/过期 → 抛异常

                List<String> whiteList = authProperties.getAdminWhiteList();
                // 如果是 refresh token，且当前请求不是 /auth/refresh → 拒绝
                String tokenType = payload.tokenType();
                if (TokenTypeEnum.REFRESH_TOKEN.getType().equals(tokenType)
                        && !whiteList.contains(request.getRequestURI())) {
                    filterChain.doFilter(request, response); // 走 401 return ;
                    return;
                }

                // 注销黑名单校验：命中就当没登录，交给入口点返回 401
                if (tokenBlackService.isBlackList(payload.jti())) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Long sysUserId = payload.userId();
                String username = payload.username();

                // JWT 里存的 authority 已经带好前缀（ROLE_xxx 或 PERMISSION_xxx），直接使用即可
                List<SimpleGrantedAuthority> authorities = payload.roles().stream()
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
                request.setAttribute(AuthTokenUtils.AUTH_FAIL_REASON_ATTR, AuthTokenUtils.REASON_EXPIRED);
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                // 其他解析失败（篡改/签名不符/格式错）：清空上下文，不在这里返回错误，
                // 让后面的授权规则判定为"未认证" → ExceptionTranslationFilter 走 401
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response); // ★ 无论如何都要放行，交给链上后续过滤器
    }
}
