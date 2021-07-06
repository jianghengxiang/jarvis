package com.jhxstudio.modules.app.cache.repo;

import com.jhxstudio.common.utils.R;
import com.jhxstudio.modules.everyday.dao.WordDao;
import com.jhxstudio.modules.everyday.entity.WordEntity;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author jhx
 * @date 2020/6/19
 **/
@Component
@Slf4j
public class WordIdCache {

    @Autowired
    private WordDao wordDao;

    @Autowired
    private UserWordCache userWordCache;

    private List<Integer> ids = new ArrayList<>();

    @PostConstruct
    public void init() {
        reset();
    }


    public void reset() {
        ids = wordDao.selectList(null).stream().map(WordEntity::getId).collect(Collectors.toList());
        log.info("软文id列表已充值");
    }

    public List<Integer> getIds() {
        return ids;
    }

    public WordEntity randomWordEntity(String key) {
        List<Integer> readIds = userWordCache.getAllIds(key);
        List<Integer> noReadIds = new ArrayList<>(ids);
        noReadIds.removeAll(readIds);
        Random random = new Random();
        Integer id = null;
        if (noReadIds.size() > 0) {
            int idx = random.nextInt(noReadIds.size());
            id = noReadIds.get(idx);
        }else{
            int idx = random.nextInt(ids.size());
            id = ids.get(idx);
            userWordCache.reset(key);
        }
        WordEntity entity = wordDao.selectById(id);
        userWordCache.add(key, id);
        return entity;
    }

}

