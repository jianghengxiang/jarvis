package com.jhxstudio.modules.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author jhx
 * @date 2021/7/6
 **/
@Data
@TableName("s_book_source")
public class BookSourceEntity {

    private Integer id;
    private String key;
    private String regex;

    private Integer status;
    private Date updateTime;
}
