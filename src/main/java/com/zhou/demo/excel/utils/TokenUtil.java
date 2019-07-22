package com.zhou.demo.excel.utils;

import java.util.UUID;

public abstract class TokenUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
