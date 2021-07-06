package com.jhxstudio.modules.spider.controller;

import com.jhxstudio.common.utils.R;
import com.jhxstudio.modules.spider.service.MzituService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jhx
 * @date 2020/6/18
 **/
@RestController
@RequestMapping("/spider")
public class SpiderController {


    @Autowired
    private MzituService mzituService;


    @RequestMapping("/mzitu")
    public R startMzitu() {
        mzituService.crawlerMzitu();
        return R.ok();
    }


    @RequestMapping("/test")
    public R test() {
        return R.ok();
    }
}
