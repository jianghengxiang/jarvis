package com.jhxstudio.modules.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author jhx
 * @date 2021/7/6
 **/
@Data
@TableName("s_book_crawler_info")
public class BookCrawlerInfoEntity {

    private Integer id;
    private String bookId;
    private String bookSource;

    private String bookUrl;
    private Integer status;
    private Date updateTime;

}
