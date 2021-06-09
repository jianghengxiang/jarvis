package com.jhxstudio.jarvis.config;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jhx
 * @date 2021/6/8
 **/
@Slf4j
public class TestBean {


    public TestBean() {
        log.info("-------------TestBean已注册---------");
    }

    public String get() {
        return "ok";
    }

}
