package com.libiao.lblearn1.security.filter;

import com.libiao.lblearn1.common.enums.TokenTypeEnum;
import com.libiao.lblearn1.common.model.TokenPayload;
import com.libiao.lblearn1.common.properties.AuthProperties;
import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.common.utils.AuthTokenUtils;
import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.redis.TokenBlackService;
import com.libiao.lblearn1.security.clientUser.LoginClientUser;

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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClientUserAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;

    private final TokenBlackService tokenBlackService;

    private final AuthProperties authProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = AuthTokenUtils.resolveToken(request);

        // 只有「拿到 token」且「当前还没认证过」才处理，避免覆盖前面过滤器已放好的身份
        if (StringUtils.isNotBlank(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                TokenPayload payload = JwtUtil.parseToken(token, jwtProperties.getClient().getSecret()); // 签名不对/过期 → 抛异常

                // 如果是 refresh token，且当前请求不是刷新接口 → 拒绝（放行后由入口点返回 401）
                String tokenType = payload.tokenType();
                if (TokenTypeEnum.REFRESH_TOKEN.getType().equals(tokenType)
                        && !authProperties.getClientWhiteList().contains(request.getRequestURI())) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // 注销黑名单校验：命中就当没登录
                if (tokenBlackService.isBlackList(payload.jti())) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // 客户端登录态：token 里不含密码，构造时传 null；已通过 token 校验，status 视为正常
                LoginClientUser principal = new LoginClientUser(payload.userId(), payload.username(), null, 1);

                // ★ Security 6 推荐写法：新建空 Context 再 set，避免复用线程上的残留身份
                // 客户端暂时不涉及权限，authorities 传空集合
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
                SecurityContextHolder.setContext(context);

            } catch (ExpiredJwtException e) {
                // accessToken 已过期：标记失败原因，交给入口点返回 401，前端据此用 refreshToken 换新 token
                request.setAttribute(AuthTokenUtils.AUTH_FAIL_REASON_ATTR, AuthTokenUtils.REASON_EXPIRED);
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                // 其他解析失败（篡改/签名不符/格式错）：清空上下文，让后续判定为"未认证" → 401
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response); // ★ 无论如何都要放行，交给链上后续过滤器
    }
}
