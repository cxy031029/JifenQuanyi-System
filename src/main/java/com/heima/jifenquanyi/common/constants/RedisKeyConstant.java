package com.heima.jifenquanyi.common.constants;

import java.time.LocalDate;

public class RedisKeyConstant {

    public static final String SESSION_PREFIX = "jf:session:";
    public static final String ADMIN_SESSION_PREFIX = "jf:admin:session:";
    public static final String SIGN_PREFIX = "jf:sign:";
    public static final String SIGN_LOCK_PREFIX = "jf:sign:lock:";
    public static final String TASK_CLAIM_PREFIX = "jf:task:claim:";
    public static final String TASK_LOCK_PREFIX = "jf:task:lock:";
    public static final String STOCK_PREFIX = "jf:stock:";
    public static final String SOLD_OUT_PREFIX = "jf:soldout:";
    public static final String SECKILL_USER_LOCK_PREFIX = "jf:seckill:user:lock:";
    public static final String POINT_BALANCE_PREFIX = "jf:point:balance:";
    public static final String PRODUCT_PREFIX = "jf:product:";
    public static final String POINT_ACCOUNT_LOCK_PREFIX = "jf:point:account:lock:";
    public static final String POINT_EXPIRE_RUN_LOCK = "jf:point:expire:run";

    public static String session(String token) {
        return SESSION_PREFIX + token;
    }

    public static String adminSession(String token) {
        return ADMIN_SESSION_PREFIX + token;
    }

    public static String sign(Long userId, LocalDate date) {
        return SIGN_PREFIX + userId + ":" + date;
    }

    public static String signLock(Long userId, LocalDate date) {
        return SIGN_LOCK_PREFIX + userId + ":" + date;
    }

    public static String taskClaim(Long userId, Long taskId, String date) {
        return TASK_CLAIM_PREFIX + userId + ":" + taskId + ":" + date;
    }

    public static String taskLock(Long userId, Long taskId, String date) {
        return TASK_LOCK_PREFIX + userId + ":" + taskId + ":" + date;
    }

    public static String stock(Long activityId) {
        return STOCK_PREFIX + activityId;
    }

    public static String soldOut(Long activityId) {
        return SOLD_OUT_PREFIX + activityId;
    }

    public static String seckillUser(Long activityId, Long userId) {
        return SECKILL_USER_LOCK_PREFIX + activityId + ":" + userId;
    }

    public static String pointBalance(Long userId) {
        return POINT_BALANCE_PREFIX + userId;
    }

    public static String product(Long productId) {
        return PRODUCT_PREFIX + productId;
    }

    public static String pointAccountLock(Long userId) {
        return POINT_ACCOUNT_LOCK_PREFIX + userId;
    }
}
