package com.libiao.lblearn1.common.exception;

import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import com.libiao.lblearn1.common.result.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public Result<Void> handleBadCredentials(BadCredentialsException e) {
        return Result.Fail(400, "用户名或密码错误");  // ★统一话术，不提示"用户不存在"
    }

    @ExceptionHandler(DisabledException.class)
    public Result<Void>  handleDisabled(DisabledException e) {
        return Result.Fail(403, "账号已被禁用");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public Result<Void> handleExpired(AccountExpiredException e) {
        return Result.Fail(403, "账号已过期");
    }

    /** 业务异常：可预期，warn 级别（不刷 error 邮件/告警） */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e) {
        log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMessage());
        return Result.Fail(e.getCode(), e.getMessage());        
    }

    /** @Valid 校验失败（@RequestBody JSON 场景）——前端传参问题 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidBody(MethodArgumentNotValidException e) {
        // 把所有字段错误拼成一行：如 "username 不能为空；phone 手机号格式不正确"
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return Result.Fail(ResultCodeEnum.BAD_REQUEST.getCode(), msg);
    }

    /** @Valid 校验失败（表单/query 参数绑定场景） */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleValidForm(BindException e) {
        String msg = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return Result.Fail(ResultCodeEnum.BAD_REQUEST.getCode(), msg);
    }

    /** 缺少必填 query 参数 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingParam(MissingServletRequestParameterException e) {
        return Result.Fail(ResultCodeEnum.BAD_REQUEST.getCode(),
                "缺少必填参数: " + e.getParameterName());
    }

    /** 请求方式不对（GET 打到了 POST 接口） */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethod(HttpRequestMethodNotSupportedException e) {
        return Result.Fail(ResultCodeEnum.METHOD_NOT_ALLOWED.getCode(), ResultCodeEnum.METHOD_NOT_ALLOWED.getMsg());
    }

    /** 静态资源/路径不存在（SB3.2+ 抛 NoResourceFoundException） */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNotFound(NoResourceFoundException e) {
        return Result.Fail(ResultCodeEnum.NOT_FOUND.getCode(), ResultCodeEnum.NOT_FOUND.getMsg());
    }

    /**
     * 方法级安全拒绝（@PreAuthorize/@Secured 等）：Spring Security 6 新类型
     * Filter 链里的权限拒绝会走 AccessDeniedHandler，但方法级注解在 Controller 之后，
     * 异常会直接冒出来，这里单独处理返回 403
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public Result<Void> handleAuthorizationDenied(AuthorizationDeniedException e) {
        log.warn("权限不足: {}", e.getMessage());
        return Result.Fail(ResultCodeEnum.FORBIDDEN.getCode(), "权限不足，无法访问该接口");
    }

    /** 兜底：未知异常。⚠️ 绝不能把堆栈返回给前端（泄露内部结构，安全漏洞） */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // error 级别 + 完整堆栈：这是需要人工介入的问题，日志是唯一的排查线索
        log.error("系统异常: ", e);
        return Result.Fail(ResultCodeEnum.SYSTEM_ERROR.getCode(), ResultCodeEnum.SYSTEM_ERROR.getMsg());
    }
}
