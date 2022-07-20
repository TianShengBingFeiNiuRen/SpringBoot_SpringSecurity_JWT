DROP TABLE IF EXISTS `authority_user_role`;
CREATE TABLE `authority_user_role`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     int(11)      DEFAULT NULL,
    `role_id`     int(11)      DEFAULT NULL,
    `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT '用户角色中间表';