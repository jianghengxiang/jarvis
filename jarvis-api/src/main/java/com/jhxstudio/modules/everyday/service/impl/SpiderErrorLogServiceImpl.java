package com.jhxstudio.modules.everyday.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhxstudio.common.utils.PageUtils;
import com.jhxstudio.common.utils.Query;
import com.jhxstudio.modules.everyday.dao.WordDao;
import com.jhxstudio.modules.everyday.entity.WordEntity;
import com.jhxstudio.modules.everyday.service.SpiderErrorLogService;
import com.jhxstudio.modules.everyday.service.WordService;
import com.jhxstudio.modules.spider.dao.SpiderErrorLogDao;
import com.jhxstudio.modules.spider.entity.SpiderErrorLogEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SpiderErrorLogServiceImpl extends ServiceImpl<SpiderErrorLogDao, SpiderErrorLogEntity> implements SpiderErrorLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpiderErrorLogEntity> page = this.page(
                new Query<SpiderErrorLogEntity>().getPage(params),
                new QueryWrapper<SpiderErrorLogEntity>()
        );

        return new PageUtils(page);
    }



}