package com.jhxstudio.modules.acq.controller;

import com.jhxstudio.common.utils.IPUtils;
import com.jhxstudio.common.utils.R;
import com.jhxstudio.modules.acq.dao.Acq01Dao;
import com.jhxstudio.modules.acq.entity.Acq01Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author jhx
 * @date 2020/6/19
 **/
@RestController
@RequestMapping("/acq")
public class AcqController {

    @Autowired
    private Acq01Dao acq01Dao;

    @RequestMapping("/acq01")
    public R acq01(@RequestBody Acq01Entity acq01Entity, HttpServletRequest request) {
        String ip = IPUtils.getIpAddr(request);
        acq01Entity.setIp(ip);
        acq01Entity.setExtractTime(new Date());
        acq01Dao.insert(acq01Entity);
        return R.ok();
    }

}
