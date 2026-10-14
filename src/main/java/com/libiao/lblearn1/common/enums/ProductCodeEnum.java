package com.libiao.lblearn1.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCodeEnum {
    /** 商品不存在 */
    PRODUCT_NOT_FOUND(400, "商品不存在");

    private final Integer code;
    private final String msg;
}
