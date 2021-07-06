package com.jhxstudio.modules.app.cache.repo;

import com.jhxstudio.modules.everyday.dao.ImageDao;
import com.jhxstudio.modules.everyday.dao.WordDao;
import com.jhxstudio.modules.everyday.entity.ImageEntity;
import com.jhxstudio.modules.everyday.entity.WordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
public class ImageIdCache {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private UserImageCache userImageCache;

    private List<Integer> ids = new ArrayList<>();

    @PostConstruct
    public void init() {
        reset();
    }


    public void reset() {
        ids = imageDao.selectList(null).stream().map(ImageEntity::getId).collect(Collectors.toList());
        log.info("图片id列表已充值");
    }

    public List<Integer> getIds() {
        return ids;
    }

    public ImageEntity randomOne(String key) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<Integer> readIds = userImageCache.getAllIds(key);
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
            userImageCache.reset(key);
        }
        ImageEntity entity = imageDao.selectById(id);
        userImageCache.add(key, id);
        return entity;
    }

}

