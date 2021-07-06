package com.jhxstudio.modules.app.cache.repo;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author jhx
 * @date 2020/6/19
 **/
@Component
public class UserWordCache {


    private ConcurrentHashMap<String, ConcurrentSkipListSet<Integer>> cache = new ConcurrentHashMap<>();

    public void add(String key,Integer id) {
        if (cache.containsKey(key)) {
            cache.get(key).add(id);
        }else{
            ConcurrentSkipListSet<Integer> list = new ConcurrentSkipListSet<>();
            list.add(id);
            cache.put(key, list);
        }
    }


    public List<Integer> getAllIds(String key) {
        if (cache.containsKey(key)) {
            return new ArrayList<>(cache.get(key));
        }else{
            cache.put(key, new ConcurrentSkipListSet<>());
            return new ArrayList<>();
        }
    }

    public void reset(String key) {
        cache.remove(key);
        cache.put(key, new ConcurrentSkipListSet<>());
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
