package com.libiao.lblearn1.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCodeEnum {
    /** 注册失败 */
    REGISTER_FAILED(1000, "注册失败"),

    /** 用户不存在 */
    USER_NOT_FOUND(1001, "用户不存在"),

    /** 密码错误 */
    PASSWORD_ERROR(1002, "密码错误"),

    /** 用户名已存在 */
    USERNAME_ALREADY_EXISTS(1003, "用户名已存在");

    private final Integer code;

    private final String msg;
}
