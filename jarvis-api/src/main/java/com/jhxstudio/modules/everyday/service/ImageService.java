package com.jhxstudio.modules.everyday.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhxstudio.common.utils.PageUtils;
import com.jhxstudio.modules.everyday.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 软文表
 *
 * @author jhx
 * @email 01119235@wisedu.com
 * @date 2020-06-17 15:14:03
 */
public interface ImageService extends IService<ImageEntity> {

    PageUtils queryPage(Map<String, Object> params);

    String imageToBase64(MultipartFile file) throws IOException;


}

