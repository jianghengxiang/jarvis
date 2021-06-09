package com.jhxstudio.jarvis.common.utils;

import java.util.UUID;

/**
 * @author jhx
 */
public class UuidUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
