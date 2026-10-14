package com.libiao.lblearn1.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderCodeEnum {
    /** 订单不存在 */
    ORDER_NOT_FOUND(400, "订单不存在"),
    /** 订单删除失败 */
    ORDER_DELETE_FAILED(10001, "订单删除失败");

    private final Integer code;
    private final String msg;
}
