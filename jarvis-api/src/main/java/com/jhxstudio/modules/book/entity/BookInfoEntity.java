package com.jhxstudio.modules.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author jhx
 * @date 2021/7/6
 **/
@Data
@TableName("s_book_info")
public class BookInfoEntity {

    private Integer id;
    private String bookId;
    private String bookName;
    private String author;
    private String intro;
    private String endStatus;
    private String lastChapter;
    private String lastUpdateTime;
    private String cover;

    private String resourcePath;
    private Integer status;
    private Date createTime;
    private Date updateTime;

}
