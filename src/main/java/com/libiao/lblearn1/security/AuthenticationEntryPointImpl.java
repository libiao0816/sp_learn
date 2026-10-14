package com.libiao.lblearn1.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import com.libiao.lblearn1.common.result.Result;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// security/AuthenticationEntryPointImpl.java —— 未认证（401）
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        // 注意：这里必须自己写响应，不能 return 也不能抛给 Controller
        response.setStatus(HttpServletResponse.SC_OK); // 业务 code 承载真实语义，HTTP 状态码保持 200
        response.setContentType("application/json;charset=UTF-8");
        // ★ 区分两种情况：
        //   - accessToken 过期 → 4011：前端可用 refreshToken 换新 token 自动续期
        //   - 未登录 / token 被篡改或已注销 → 401：前端只能跳转登录页
        // 原因由 JwtAuthenticationFilter 在解析 token 失败时写入 request 属性传递过来
        boolean expired = JwtAuthenticationFilter.REASON_EXPIRED
                .equals(request.getAttribute(JwtAuthenticationFilter.AUTH_FAIL_REASON_ATTR));
        ResultCodeEnum resultCode = expired
                ? ResultCodeEnum.TOKEN_EXPIRED
                : ResultCodeEnum.AUTHENTICATION_FAILED;
        response.getWriter().write(objectMapper.writeValueAsString(
                Result.Fail(resultCode.getCode(), resultCode.getMsg())));
    }
}