/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : luckydeer

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-09-18 22:05:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for wx_app_status
-- ----------------------------
DROP TABLE IF EXISTS `wx_app_status`;
CREATE TABLE `wx_app_status` (
  `version_id` varchar(11) COLLATE utf8_unicode_ci NOT NULL COMMENT '小程序版本号码',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '小程序状态值 0.审核状态 1.正常状态',
  PRIMARY KEY (`version_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of wx_app_status
-- ----------------------------
INSERT INTO `wx_app_status` VALUES ('1.0.0', '0');
