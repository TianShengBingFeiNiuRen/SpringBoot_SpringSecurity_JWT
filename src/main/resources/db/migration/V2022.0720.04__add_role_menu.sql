DROP TABLE IF EXISTS `authority_role_menu`;
CREATE TABLE `authority_role_menu`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `role_id`     int(11)      DEFAULT NULL,
    `menu_id`     int(11)      DEFAULT NULL,
    `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT '角色菜单中间表';