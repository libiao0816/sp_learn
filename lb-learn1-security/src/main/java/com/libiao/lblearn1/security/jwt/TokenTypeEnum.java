package com.libiao.lblearn1.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenTypeEnum {
    /** 访问令牌 */
    ACCESS_TOKEN("access_token", "访问令牌"),
    /** 刷新令牌 */
    REFRESH_TOKEN("refresh_token","刷新令牌");

    private final String type;
    private final String msg;
}
