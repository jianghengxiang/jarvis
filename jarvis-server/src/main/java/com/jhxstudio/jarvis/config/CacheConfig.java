package com.jhxstudio.jarvis.config;

import com.jhxstudio.jarvis.common.aop.CacheAspect;
import com.jhxstudio.jarvis.common.observer.listener.ClearCacheListener;
import com.jhxstudio.jarvis.factory.cache.JarvisContentCache;
import com.jhxstudio.jarvis.factory.cache.JvmContentCacheImpl;
import com.jhxstudio.jarvis.factory.cache.RedisContentCacheImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jhx
 * @date 2021/6/8
 **/
@Configuration
@ConditionalOnProperty(prefix = "cache.config", name = "enable", havingValue = "true")
public class CacheConfig {


    @Bean
    public JarvisContentCache getContentCache(@Value("${cache.config.type}") String cacheConfigType) {
        if ("jvm".equals(cacheConfigType)) {
            return new JvmContentCacheImpl();
        } else if ("redis".equals(cacheConfigType)) {
            return new RedisContentCacheImpl();
        } else{
            throw new RuntimeException("初始化失败,无法初始化JarvisContentCache");
        }
    }

    @Bean
    @ConditionalOnBean(JarvisContentCache.class)
    public CacheAspect getCacheAspect(JarvisContentCache cache) {
        return new CacheAspect(cache);
    }

    @Bean
    @ConditionalOnBean(JarvisContentCache.class)
    public ClearCacheListener clearCacheListener(JarvisContentCache cache) {
        return new ClearCacheListener(cache);
    }







}
