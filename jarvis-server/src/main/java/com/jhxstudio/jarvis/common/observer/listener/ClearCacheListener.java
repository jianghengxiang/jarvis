package com.jhxstudio.jarvis.common.observer.listener;

import com.jhxstudio.jarvis.common.observer.event.ClearCacheEvent;
import com.jhxstudio.jarvis.factory.cache.JarvisContentCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * @author jhx
 * @date 2021/6/9
 **/
@Slf4j
public class ClearCacheListener implements ApplicationListener<ClearCacheEvent> {

    private final JarvisContentCache cache;

    public ClearCacheListener(JarvisContentCache cache) {
        log.info("初始化 -------- ClearCacheListener");
        this.cache = cache;
    }

    @Override
    public void onApplicationEvent(ClearCacheEvent event) {
        log.info("接收到ClearCacheEvent事件, cacheKey is: {}", event.getClearCacheKey());
        cache.del(event.getClearCacheKey());
    }
}
