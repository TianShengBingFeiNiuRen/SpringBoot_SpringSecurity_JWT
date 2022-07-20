DROP TABLE IF EXISTS `authority_user`;
CREATE TABLE `authority_user`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username`    varchar(255) DEFAULT NULL COMMENT '用户名',
    `password`    varchar(255) DEFAULT NULL COMMENT '密码',
    `email`       varchar(255) DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(255) DEFAULT NULL COMMENT '手机号',
    `valid_time`  varchar(255) DEFAULT NULL COMMENT '有效截止时间',
    `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
    `remark`      mediumtext COMMENT '备注',
    `nickname`    varchar(255) DEFAULT NULL COMMENT '昵称',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT '用户表';