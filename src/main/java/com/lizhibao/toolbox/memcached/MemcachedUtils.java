package com.lizhibao.toolbox.memcached;

import java.lang.reflect.Method;

/**
 * 工具类
 * @author lizhibao
 * @date 2025-02-14
 */
public class MemcachedUtils {
    public static void setFieldValue(String field, String value, Object clazz) {
        try {
            String setMethodName = "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
            Method setMethod = clazz.getClass().getMethod(setMethodName, String.class);
            setMethod.invoke(clazz, value.replace("\r\n", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
