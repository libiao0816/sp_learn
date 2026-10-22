package com.libiao.lblearn1.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CartCodeEnum {

    UPDATE_FAIL(500,"修改购物车数量失败");

    private final Integer code;
    private final String msg;
}
