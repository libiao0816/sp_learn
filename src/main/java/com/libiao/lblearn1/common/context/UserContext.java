package com.libiao.lblearn1.common.context;

import com.libiao.lblearn1.domain.po.User;

/**
 * 用户上下文：基于 ThreadLocal 在当前请求线程内存取登录用户
 * 纯工具类，全部为 static，无需交给 Spring 管理
 */
public class UserContext {

    private static final ThreadLocal<User> THREAD_LOCAL_USER = new ThreadLocal<>();

    private UserContext() {
    }

    public static User getUser() {
        return THREAD_LOCAL_USER.get();
    }

    public static void setUser(User user) {
        THREAD_LOCAL_USER.set(user);
    }

    public static void removeUser() {
        THREAD_LOCAL_USER.remove();
    }
}
