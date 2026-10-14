package com.libiao.lblearn1.interceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.libiao.lblearn1.common.context.UserContext;
import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.security.TokenBlackService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import com.libiao.lblearn1.common.exception.BusinessException;
import com.libiao.lblearn1.common.properties.JwtProperties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    private final TokenBlackService tokenBlackService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = request.getHeader(jwtProperties.getTokenName());

        if (Objects.isNull(token) || token.isEmpty()) {
            throw new BusinessException(ResultCodeEnum.AUTHENTICATION_FAILED.getCode(), "请先进行登陆"); // ★
        }

        Claims claims = null;
        try {
            claims = JwtUtil.parseToken(token, jwtProperties.getSecret());
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ResultCodeEnum.AUTHENTICATION_FAILED.getCode(), "token 已过期，请重新登录"); // ★
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.AUTHENTICATION_FAILED.getCode(),
                    ResultCodeEnum.AUTHENTICATION_FAILED.getMsg()); // ★
        }
        if (claims != null) {
            if(tokenBlackService.isBlackList(claims.getId())){
                throw new BusinessException(ResultCodeEnum.AUTHENTICATION_FAILED.getCode(), "token 已失效，请重新登录"); // ★
            }

//            if(handler instanceof HandlerMethod hm){
//                RequireRole anno = hm.getMethodAnnotation(RequireRole.class);
//                if(Objects.nonNull(anno)){
//                    List<?> roles = claims.get("roles", List.class);
//                    boolean pass = Arrays.stream(anno.value()).anyMatch(roles::contains);
//                    if(!pass){
//                        throw new BusinessException(ResultCodeEnum.FORBIDDEN.getCode(), ResultCodeEnum.FORBIDDEN.getMsg()); // ★
//                    }
//                }
//            }

            UserContext.setUser(User.builder().id(Long.parseLong(claims.getSubject()))
                    .username(claims.get("username").toString()).build());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        UserContext.removeUser();
    }

}
