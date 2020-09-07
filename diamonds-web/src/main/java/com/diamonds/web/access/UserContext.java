package com.diamonds.web.access;

import com.diamonds.server.domin.UserInfoCache;

public class UserContext {

    private static final ThreadLocal<UserInfoCache> USER_INFO_CACHE_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(UserInfoCache user) {
        USER_INFO_CACHE_THREAD_LOCAL.set(user);
    }

    public static UserInfoCache getUser() {
        return USER_INFO_CACHE_THREAD_LOCAL.get();
    }

    public static void removeUser() {
        USER_INFO_CACHE_THREAD_LOCAL.remove();
    }
}
