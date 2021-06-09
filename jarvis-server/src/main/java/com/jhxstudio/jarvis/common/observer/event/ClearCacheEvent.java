package com.jhxstudio.jarvis.common.observer.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author jhx
 * @date 2021/6/9
 **/
public class ClearCacheEvent  extends ApplicationEvent {

    private final String clearCacheKey;

    public ClearCacheEvent(Object source, String clearCacheKey) {
        super(source);
        this.clearCacheKey = clearCacheKey;
    }

    public String getClearCacheKey() {
        return clearCacheKey;
    }
}
