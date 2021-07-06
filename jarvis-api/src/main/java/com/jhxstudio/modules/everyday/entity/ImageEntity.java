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
@TableName("e_image")
public class ImageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;

	private String imageName;

	private String imageSeries;

	private String remark;

	private Integer imageIndex;

	private String originImageUrl;

	/**
	 * 存储类型，0 链接，1 base64，2 oss
	 */
	private Integer storeType;
	/**
	 * 图片内容
	 */
	private String storeContent;
	/**
	 * 来源
	 */
	private String imageFrom;
	/**
	 * 图片分类
	 */
	private String category;
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
