package com.f4n.blog.utils;

import com.f4n.blog.dao.pojo.SysUser;

/*实现线程变量隔离*/
public class UserThreadLocal {
    private UserThreadLocal() {
    }
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser) {
        LOCAL.set(sysUser);
    }

    public static SysUser get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }
}
