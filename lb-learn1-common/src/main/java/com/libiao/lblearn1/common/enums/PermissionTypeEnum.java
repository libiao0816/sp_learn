package com.libiao.lblearn1.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionTypeEnum {

    /** 页面级权限：控制页面/菜单是否可见 */
    PAGE(1, "页面级"),

    /** 方法级权限：控制接口方法是否可调用 */
    METHOD(2, "方法级"),

    /** 组件级权限：控制页面内按钮等组件是否可用 */
    COMPONENT(3, "组件级");

    private final Integer code;

    private final String msg;
}
