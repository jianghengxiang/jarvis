package com.jhxstudio.modules.spider.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 软文表
 * 
 * @author jhx
 * @email 01119235@wisedu.com
 * @date 2020-06-17 15:14:03
 */
@Data
@TableName("e_spider_error_log")
public class SpiderErrorLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	private String url;
	private String remark;

	private String spiderTarget;
	/**
	 * 创建时间
	 */
	private Date createTime;


}
