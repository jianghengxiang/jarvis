package com.jhxstudio.modules.everyday.controller;

import java.util.Arrays;
import java.util.Map;

import com.jhxstudio.modules.app.cache.repo.WordIdCache;
import com.jhxstudio.modules.everyday.entity.WordEntity;
import com.jhxstudio.modules.everyday.service.WordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhxstudio.common.utils.PageUtils;
import com.jhxstudio.common.utils.R;



/**
 * 软文表
 *
 * @author jhx
 * @email 01119235@wisedu.com
 * @date 2020-06-17 15:14:03
 */
@RestController
@RequestMapping("everyday/word")
public class WordController {
    @Autowired
    private WordService wordService;

    @Autowired
    private WordIdCache cache;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("everyday:word:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("everyday:word:info")
    public R info(@PathVariable("id") Integer id){
		WordEntity word = wordService.getById(id);

        return R.ok().put("word", word);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("everyday:word:save")
    public R save(@RequestBody WordEntity word){
		wordService.save(word);
        cache.reset();
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("everyday:word:update")
    public R update(@RequestBody WordEntity word){
		wordService.updateById(word);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("everyday:word:delete")
    public R delete(@RequestBody Integer[] ids){
		wordService.removeByIds(Arrays.asList(ids));
        cache.reset();
        return R.ok();
    }

}
