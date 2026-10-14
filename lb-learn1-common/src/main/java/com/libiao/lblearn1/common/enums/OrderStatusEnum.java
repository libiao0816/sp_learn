package com.libiao.lblearn1.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /** 待支付 */
    PENDING(0, "待支付"),

    /** 已支付 */
    PAID(1, "已支付"),

    /** 已发货 */
    SHIPPED(2, "已发货"),

    /** 已完成 */
    COMPLETED(3, "已完成"),

    /** 已取消 */
    CANCELLED(4, "已取消");

    private final Integer code;
    private final String msg;
}
