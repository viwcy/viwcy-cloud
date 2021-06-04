/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.26 : Database - viwcy_user
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`viwcy_user` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `viwcy_user`;

/*Table structure for table `undo_log` */

DROP TABLE IF EXISTS `undo_log`;

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `undo_log` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `head_photo` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `phone` char(11) DEFAULT NULL COMMENT '联系电话(可做登录账号使用)',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱(可做登录账号使用)',
  `password` varchar(100) DEFAULT NULL COMMENT '用户密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`user_name`,`head_photo`,`phone`,`email`,`password`,`create_time`,`update_time`) values (52,'小鹿',NULL,'15268987381','3386216884@qq.com','$2a$10$x2pz6oEjiCaqZfjpMpeOiO5SZ//yEAhSVnKC/7rojDIpBmxzd1K1S','2021-05-27 16:43:41','2021-05-27 16:43:41'),(53,'小飞象',NULL,'15268987382','3386216881@qq.com','$2a$10$Fa2Fa5sM/S4d5eFBe.kSFeZwJ6CDs9MMYOp011L0f8fM/1qhAgFsO','2021-05-27 16:45:26','2021-05-27 16:45:26');

/*Table structure for table `user_article` */

DROP TABLE IF EXISTS `user_article`;

CREATE TABLE `user_article` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户主键ID',
  `title` varchar(64) DEFAULT NULL COMMENT '文章标题',
  `content` text COMMENT '文章内容',
  `create_id` bigint(11) DEFAULT NULL,
  `create_name` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_id` bigint(11) DEFAULT NULL,
  `update_name` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

/*Data for the table `user_article` */

insert  into `user_article`(`id`,`user_id`,`title`,`content`,`create_id`,`create_name`,`create_time`,`update_id`,`update_name`,`update_time`) values (43,52,'上海欢迎您','哈哈哈哈，我来自中国北京，人称帝都',52,'小鹿','2021-05-27 17:03:53',52,'小鹿','2021-05-27 17:03:53'),(44,52,'北京欢迎您','哈哈哈哈，我来自中国上海，人称魔都',52,'小鹿','2021-05-28 09:14:31',52,'小鹿','2021-05-28 09:14:31');

/*Table structure for table `user_wallet` */

DROP TABLE IF EXISTS `user_wallet`;

CREATE TABLE `user_wallet` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户表主键ID',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名称',
  `is_vip` tinyint(2) DEFAULT '1' COMMENT '是否会员，1否，2是',
  `now_balance` decimal(16,2) DEFAULT '0.00' COMMENT '当前余额，人民币，单位元',
  `now_integral` decimal(16,2) DEFAULT '0.00' COMMENT '当前积分',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

/*Data for the table `user_wallet` */

insert  into `user_wallet`(`id`,`user_id`,`user_name`,`is_vip`,`now_balance`,`now_integral`,`create_time`,`update_time`) values (44,52,'小鹿',1,'0.00','0.00','2021-05-27 16:43:41','2021-05-31 11:04:31'),(45,53,'小飞象',1,'0.00','0.00','2021-05-27 16:45:26','2021-05-31 11:04:38');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
