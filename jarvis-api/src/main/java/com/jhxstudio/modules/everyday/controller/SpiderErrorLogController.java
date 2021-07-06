package com.jhxstudio.modules.everyday.controller;

import com.jhxstudio.common.utils.PageUtils;
import com.jhxstudio.common.utils.R;
import com.jhxstudio.modules.everyday.service.SpiderErrorLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author jhx
 * @date 2020/6/19
 **/
@RestController
@RequestMapping("/everyday/log")
public class SpiderErrorLogController {


    @Autowired
    private SpiderErrorLogService spiderErrorLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("everyday:image:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spiderErrorLogService.queryPage(params);

        return R.ok().put("page", page);
    }


}
