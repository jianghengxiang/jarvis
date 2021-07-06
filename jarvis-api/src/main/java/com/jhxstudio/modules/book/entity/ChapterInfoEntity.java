package com.jhxstudio.modules.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author jhx
 * @date 2021/7/6
 **/
@Data
@TableName("s_chapter_info")
public class ChapterInfoEntity {

    private Integer id;
    private String bookId;
    private String chapterIndex;
    private String chapterName;
    private String content;

    private String resourcePath;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
