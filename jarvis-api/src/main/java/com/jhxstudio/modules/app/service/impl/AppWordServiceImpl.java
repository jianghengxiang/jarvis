package com.jhxstudio.modules.app.service.impl;

import com.jhxstudio.common.utils.HttpContextUtils;
import com.jhxstudio.common.utils.IPUtils;
import com.jhxstudio.modules.app.cache.repo.WordIdCache;
import com.jhxstudio.modules.app.service.AppWordService;
import com.jhxstudio.modules.everyday.entity.WordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jhx
 * @date 2020/6/19
 **/
@Service
public class AppWordServiceImpl implements AppWordService {

    @Autowired
    private WordIdCache cache;

    @Override
    public WordEntity randomWordEntity() {
        String ip = IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        return cache.randomWordEntity(ip);
    }
}
