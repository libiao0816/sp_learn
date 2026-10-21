package com.libiao.lblearn1.security.util;

import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import com.libiao.lblearn1.common.exception.BusinessException;
import com.libiao.lblearn1.security.clientUser.LoginClientUser;
import com.libiao.lblearn1.security.sysUser.LoginSysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 统一获取当前登录用户。
 * <p>
 * 管理端（/admin/**）的 principal 是 {@link LoginSysUser}，C 端（/client/**）的是 {@link LoginClientUser}，
 * 两条过滤链各自往 SecurityContext 里放不同对象。domain service 不关心请求来自哪一端，
 * 统一从这里取当前用户 id，避免在 service 里强转某一种 principal。
 */
public final class LoginUserUtils {

    private LoginUserUtils() {
    }

    /** 当前登录用户 id；未登录时抛 401。 */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication == null ? null : authentication.getPrincipal();
        if (principal instanceof LoginSysUser sysUser) {
            return sysUser.getId();
        }
        if (principal instanceof LoginClientUser clientUser) {
            return clientUser.getId();
        }
        throw new BusinessException(ResultCodeEnum.AUTHENTICATION_FAILED);
    }
}
