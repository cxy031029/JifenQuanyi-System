package com.heima.jifenquanyi.common.util;

import com.heima.jifenquanyi.common.exception.BizException;

public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        Long userId = USER_ID.get();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        return userId;
    }

    public static void clear() {
        USER_ID.remove();
    }
}
