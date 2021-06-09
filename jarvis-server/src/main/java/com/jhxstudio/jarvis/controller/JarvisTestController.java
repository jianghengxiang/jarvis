package com.jhxstudio.jarvis.controller;

import com.jhxstudio.jarvis.common.annotation.JsonParam;
import com.jhxstudio.jarvis.common.annotation.NeedCache;
import com.jhxstudio.jarvis.common.observer.event.ClearCacheEvent;
import com.jhxstudio.jarvis.common.utils.R;
import com.jhxstudio.jarvis.entity.JarvisTest;
import com.jhxstudio.jarvis.service.JarvisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jhx
 * @date 2021/6/8
 **/
@RestController
@RequestMapping("/jarvis/test")
public class JarvisTestController {


    @Autowired
    private JarvisTestService service;

    @RequestMapping("insert")
    public R insert(@JsonParam("name") String name) {
        JarvisTest jarvisTest = new JarvisTest();
        jarvisTest.setName(name);
        service.save(jarvisTest);
        return R.ok(jarvisTest);
    }

    @RequestMapping("/list")
    @NeedCache
    public R list() {
        return R.ok(service.list());
    }


    @RequestMapping("/get")
    public R getById(Integer id) {
        return R.ok(service.getById(id));
    }

    @RequestMapping("/rm/{id}")
    public R delete(@PathVariable Integer id) {
        return R.ok(service.removeById(id));
    }

    @Autowired
    private ApplicationEventPublisher publisher;
    ApplicationContext context;
    @RequestMapping("/clearCache")
    public R clearCache() {
        publisher.publishEvent(new ClearCacheEvent(this, "jarvis:content:cache:/jarvis/test/list?null"));
        return R.ok();
    }

}
