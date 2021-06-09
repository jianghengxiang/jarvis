package com.jhxstudio.jarvis.factory.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jhx
 * @date 2021/6/8
 **/
@Slf4j
public class JvmContentCacheImpl implements JarvisContentCache{


    public JvmContentCacheImpl () {
        log.info("初始化 ----- JvmContentCacheImpl");
    }

    private final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();


    @Override
    public void set(String key, String content) {
        map.put(key, content);
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public void del(String key) {
        map.remove(key);
    }
}
