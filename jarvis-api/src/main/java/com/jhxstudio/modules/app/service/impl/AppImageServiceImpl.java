package com.jhxstudio.modules.app.service.impl;

import com.jhxstudio.common.utils.HttpContextUtils;
import com.jhxstudio.common.utils.IPUtils;
import com.jhxstudio.modules.app.cache.repo.ImageIdCache;
import com.jhxstudio.modules.app.service.AppImageService;
import com.jhxstudio.modules.everyday.entity.ImageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jhx
 * @date 2020/6/19
 **/
@Service
public class AppImageServiceImpl implements AppImageService {


    @Autowired
    private ImageIdCache cache;

    @Override
    public String randomImage() {
        String ip = IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        ImageEntity entity = cache.randomOne(ip);
        if (entity == null) {
            return null;
        }
        return "data:image/jpeg;base64,"+entity.getStoreContent();
    }
}
