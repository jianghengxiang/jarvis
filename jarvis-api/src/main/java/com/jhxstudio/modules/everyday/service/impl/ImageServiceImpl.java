package com.jhxstudio.modules.everyday.service.impl;

import com.jhxstudio.common.exception.RRException;
import com.jhxstudio.modules.everyday.dao.ImageDao;
import com.jhxstudio.modules.everyday.entity.ImageEntity;
import com.jhxstudio.modules.everyday.service.ImageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhxstudio.common.utils.PageUtils;
import com.jhxstudio.common.utils.Query;
import org.springframework.web.multipart.MultipartFile;


@Service("imageService")
public class ImageServiceImpl extends ServiceImpl<ImageDao, ImageEntity> implements ImageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ImageEntity> page = this.page(
                new Query<ImageEntity>().getPage(params),
                new QueryWrapper<ImageEntity>().select(
                        "store_type","image_from","category",
                        "image_name","image_index","remark",
                        "image_series","origin_image_url","status",
                        "create_time","update_time"
                )
        );

        return new PageUtils(page);
    }

    @Override
    public String imageToBase64(MultipartFile file) throws IOException {
        if (file == null || file.getSize() == 0) {
            throw new RRException("上传图片为空");
        }
        String base64Prefix = "data:image/%s;base64,";
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new RRException("上传图片名称为空");
        }
        String fileSuffix = file.getOriginalFilename().substring(
                file.getOriginalFilename().lastIndexOf(".") + 1
        );
        Base64.Encoder encoder = Base64.getEncoder();
        String imageEncode = encoder.encodeToString(file.getBytes());

        return String.format(base64Prefix, fileSuffix) + imageEncode;
    }

}