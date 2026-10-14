package com.libiao.lblearn1.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {
    /** 操作成功 */
    SUCCESS(200, "操作成功"),
    /** 系统繁忙，请稍后重试 */
    SYSTEM_ERROR(500, "系统繁忙，请稍后重试"),
    /** 请求参数错误 */
    BAD_REQUEST(400, "请求参数错误"),
    /** 请求方法不允许 */
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),
    /** 认证失败（未登录 / token 被篡改或已注销，需重新登录） */
    AUTHENTICATION_FAILED(401, "认证失败"),
    /** 登录已过期（accessToken 过期，前端可用 refreshToken 换新 token） */
    TOKEN_EXPIRED(4011, "登录已过期"),
    /** 权限不足 */
    FORBIDDEN(403, "权限不足");

    private final Integer code;
    private final String msg;
}
