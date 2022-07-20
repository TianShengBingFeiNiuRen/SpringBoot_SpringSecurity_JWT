DROP TABLE IF EXISTS `authority_role`;
CREATE TABLE `authority_role`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `role_name`    varchar(255) DEFAULT NULL COMMENT '角色名称(必须以ROLE_起始命名)',
    `role_name_CN` varchar(255) DEFAULT NULL COMMENT '角色名称中文',
    `update_time`  varchar(255) DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT '角色表';