package com.jhxstudio.jarvis.common.utils;

import java.util.HashMap;


/**
 * Map工具类
 *
 * @author zhangsuhui
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
