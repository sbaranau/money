/*
 Navicat MySQL Data Transfer

 Source Server         : mysql
 Source Server Version : 50613
 Source Host           : localhost
 Source Database       : osdbm

 Target Server Version : 50613
 File Encoding         : utf-8

 Date: 08/15/2013 06:54:59 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `PRICE`
-- ----------------------------
DROP TABLE IF EXISTS `PRICE`;
CREATE TABLE `PRICE` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `bankId` int(11) NOT NULL,
  `date` int(11) DEFAULT NULL,
  `time` int(11) DEFAULT NULL,
  `moneyName` varchar(255) DEFAULT NULL,
  `sell` decimal(10,0) DEFAULT NULL,
  `buy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `bank`
-- ----------------------------
DROP TABLE IF EXISTS `bank`;
CREATE TABLE `bank` (
  `idbank` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `namebank` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idbank`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
