package com.jhxstudio.modules.everyday.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 软文表
 * 
 * @author jhx
 * @email 01119235@wisedu.com
 * @date 2020-06-17 15:14:03
 */
@Data
@TableName("e_word")
public class WordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 软文类型，0 图片，1 文字
	 */
	private Integer type;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 来源
	 */
	private String contentFrom;
	/**
	 * 配图
	 */
	private String pic;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
