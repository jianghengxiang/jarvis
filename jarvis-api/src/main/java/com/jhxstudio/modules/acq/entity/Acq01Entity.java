package com.jhxstudio.modules.acq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author jhx
 * @date 2020/6/19
 **/
@Data
@TableName("t_sea_acq01")
public class Acq01Entity {
    /*
    CREATE TABLE `t_sea_acq01` (
  `source` varchar(100) DEFAULT NULL COMMENT '渠道',
  `app_id` varchar(100) DEFAULT NULL COMMENT '产品标志',
  `app_version` varchar(100) DEFAULT NULL COMMENT 'app版本',
  `dev_type` varchar(100) DEFAULT NULL COMMENT '设备类型，iOS，android，pc',
  `mid` varchar(100) DEFAULT NULL COMMENT 'ACQ01',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
  `refer_id` varchar(100) DEFAULT NULL COMMENT '页面跳转id',
  `refer` varchar(100) DEFAULT NULL COMMENT '页面跳转',
  `page_id` varchar(100) DEFAULT NULL COMMENT '页面id',
  `path` varchar(100) DEFAULT NULL COMMENT '页面路由',
  `link_id` varchar(100) DEFAULT NULL COMMENT '链路id',
  `finish_time` datetime DEFAULT NULL COMMENT '页面完成时间',
  `params` varchar(255) DEFAULT NULL COMMENT '参数',
  `ip` int(10) unsigned DEFAULT NULL COMMENT '请求ip',
  `extract_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上报时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
     */

    private String source;

    private String appId;

    private String appVersion;

    private String devType;

    private String mid;

    private String userId;

    private String referId;

    private String refer;

    private String pageId;

    private String path;

    private String linkId;

    private String finishTime;

    private String params;

    private String ip;

    private Date extractTime;
}
