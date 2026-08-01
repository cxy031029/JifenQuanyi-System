package com.heima.jifenquanyi.common.util;

import cn.hutool.core.util.RandomUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String next(String prefix) {
        return prefix + LocalDateTime.now().format(FORMATTER) + RandomUtil.randomNumbers(6);
    }
}
