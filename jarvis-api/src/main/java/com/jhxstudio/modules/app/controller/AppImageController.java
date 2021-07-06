package com.jhxstudio.modules.app.controller;

import com.jhxstudio.common.utils.R;
import com.jhxstudio.modules.app.service.AppImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jhx
 * @date 2020/6/19
 **/
@RestController
@RequestMapping("/app")
public class AppImageController {


    @Autowired
    private AppImageService appImageService;


    @RequestMapping("/image")
    public R randomOneImage() {
        return R.ok().put("url", appImageService.randomImage());
    }

}
