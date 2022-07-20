DROP TABLE IF EXISTS `authority_menu`;
CREATE TABLE `authority_menu`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `url`         varchar(255) DEFAULT NULL COMMENT '请求路径(/**结尾)',
    `menu_name`   varchar(255) DEFAULT NULL COMMENT '菜单名称',
    `parent_id`   int(11)      DEFAULT NULL COMMENT '父菜单id',
    `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(255) DEFAULT NULL COMMENT '备注',
    `url_pre`     varchar(255) DEFAULT NULL COMMENT '路由(前端自己匹配用)',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT '菜单表';