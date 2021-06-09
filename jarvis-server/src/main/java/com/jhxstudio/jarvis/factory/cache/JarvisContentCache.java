package com.jhxstudio.jarvis.factory.cache;

/**
 * @author jhx
 * @date 2021/6/8
 **/
public interface JarvisContentCache {


    void set(String key, String content);

    String get(String key);

    void del(String key);


}
