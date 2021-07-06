package com.jhxstudio.modules.everyday.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.jhxstudio.modules.app.cache.repo.ImageIdCache;
import com.jhxstudio.modules.everyday.entity.ImageEntity;
import com.jhxstudio.modules.everyday.service.ImageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhxstudio.common.utils.PageUtils;
import com.jhxstudio.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 软文表
 *
 * @author jhx
 * @email 01119235@wisedu.com
 * @date 2020-06-17 15:14:03
 */
@RestController
@RequestMapping("everyday/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @RequestMapping("/toBase64")
    public R toBase64(@RequestParam("file") MultipartFile file) throws IOException {
        return R.ok().put("url", imageService.imageToBase64(file));
    }

    @Autowired
    private ImageIdCache imageIdCache;

    @RequestMapping("/refreshCache")
    public R refreshImageIdCache() {
        imageIdCache.reset();
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("everyday:image:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = imageService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("everyday:image:info")
    public R info(@PathVariable("id") Integer id){
		ImageEntity image = imageService.getById(id);

        return R.ok().put("image", image);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("everyday:image:save")
    public R save(@RequestBody ImageEntity image){
		imageService.save(image);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("everyday:image:update")
    public R update(@RequestBody ImageEntity image){
		imageService.updateById(image);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("everyday:image:delete")
    public R delete(@RequestBody Integer[] ids){
		imageService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
