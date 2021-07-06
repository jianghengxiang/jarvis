package com.jhxstudio.modules.everyday.service.impl;

import com.jhxstudio.modules.everyday.dao.WordDao;
import com.jhxstudio.modules.everyday.entity.WordEntity;
import com.jhxstudio.modules.everyday.service.WordService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhxstudio.common.utils.PageUtils;
import com.jhxstudio.common.utils.Query;



@Service("wordService")
public class WordServiceImpl extends ServiceImpl<WordDao, WordEntity> implements WordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WordEntity> page = this.page(
                new Query<WordEntity>().getPage(params),
                new QueryWrapper<WordEntity>()
        );

        return new PageUtils(page);
    }



}