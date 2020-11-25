/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 121.89.178.219:3306
 Source Schema         : gateway

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 05/11/2020 14:26:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for filter
-- ----------------------------
DROP TABLE IF EXISTS `filter`;
CREATE TABLE `filter`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `filter_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '过滤器名称',
  `filter_val` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '过滤器的值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of filter
-- ----------------------------
INSERT INTO `filter` VALUES (1, 'StripPrefix', '1');
INSERT INTO `filter` VALUES (2, 'StripPrefix', '2');
INSERT INTO `filter` VALUES (3, 'ValidateCodeFilter', NULL);
INSERT INTO `filter` VALUES (4, 'PasswordDecoderFilter', NULL);

-- ----------------------------
-- Table structure for predicate
-- ----------------------------
DROP TABLE IF EXISTS `predicate`;
CREATE TABLE `predicate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `predicate_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `predicate_val` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of predicate
-- ----------------------------
INSERT INTO `predicate` VALUES (1, 'Path', '/test/**');
INSERT INTO `predicate` VALUES (2, 'Path', '/sentinel/**');
INSERT INTO `predicate` VALUES (3, 'After', '2019-09-24T16:30:00+08:00[Asia/Shanghai]');
INSERT INTO `predicate` VALUES (4, 'Path', '/custom/**');
INSERT INTO `predicate` VALUES (5, 'Path', '/auth/**');
INSERT INTO `predicate` VALUES (6, 'Path', '/sys/**');
INSERT INTO `predicate` VALUES (7, 'Path', '/log/**');
INSERT INTO `predicate` VALUES (8, 'Path', '/gen/**');
INSERT INTO `predicate` VALUES (9, 'Path', '/nacos/**');
INSERT INTO `predicate` VALUES (10, 'Path', '/xxl-job-admin/**');
INSERT INTO `predicate` VALUES (11, 'Path', '/mnt/**');
INSERT INTO `predicate` VALUES (12, 'Path', '/tool/**');

-- ----------------------------
-- Table structure for router
-- ----------------------------
DROP TABLE IF EXISTS `router`;
CREATE TABLE `router`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `router_id` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `router_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `router_url` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL,
  `router_type` tinyint(1) NOT NULL,
  `threshold` bigint(20) NOT NULL,
  `interval_sec` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of router
-- ----------------------------
INSERT INTO `router` VALUES (1, 'mygateway', 'aaaa', 'test-serve', 0, 0, 0, 1);
INSERT INTO `router` VALUES (2, 'mysentinel', 'bbb', 'sentinel-server', 0, 0, 0, 1);
INSERT INTO `router` VALUES (3, 'custom', 'cccc', 'https://www.baidu.com/', 0, 1, 1, 1);
INSERT INTO `router` VALUES (4, 'auth-server', 'auth', 'auth-server', 1, 0, 1, 1);
INSERT INTO `router` VALUES (5, 'sys-server', 'system', 'system-server', 1, 0, 100, 1);
INSERT INTO `router` VALUES (6, 'log-server', 'log', 'log-server', 1, 0, 100, 1);
INSERT INTO `router` VALUES (7, 'gen-server', 'gen', 'gen-server', 1, 0, 100, 1);
INSERT INTO `router` VALUES (18, 'nacos', 'nacos-server', 'http://127.0.0.1:8848', 1, 1, 100, 1);
INSERT INTO `router` VALUES (19, 'xxl-job', 'xxljob', 'http://localhost:8088', 1, 1, 100, 1);
INSERT INTO `router` VALUES (20, 'mnt', 'mnt-server', 'mnt-server', 1, 0, 100, 5);
INSERT INTO `router` VALUES (21, 'tool-server', 'tool', 'tool-server', 1, 0, 100, 5);

-- ----------------------------
-- Table structure for router_filter
-- ----------------------------
DROP TABLE IF EXISTS `router_filter`;
CREATE TABLE `router_filter`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `router_id` bigint(20) NOT NULL,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of router_filter
-- ----------------------------
INSERT INTO `router_filter` VALUES (1, 1, 1);
INSERT INTO `router_filter` VALUES (2, 2, 1);
INSERT INTO `router_filter` VALUES (4, 4, 1);
INSERT INTO `router_filter` VALUES (5, 4, 3);
INSERT INTO `router_filter` VALUES (6, 4, 4);
INSERT INTO `router_filter` VALUES (7, 5, 1);
INSERT INTO `router_filter` VALUES (8, 6, 1);
INSERT INTO `router_filter` VALUES (9, 7, 1);
INSERT INTO `router_filter` VALUES (23, 3, 1);
INSERT INTO `router_filter` VALUES (24, 20, 1);
INSERT INTO `router_filter` VALUES (25, 21, 1);

-- ----------------------------
-- Table structure for router_predicate
-- ----------------------------
DROP TABLE IF EXISTS `router_predicate`;
CREATE TABLE `router_predicate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `router_id` bigint(20) NOT NULL,
  `predicate_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of router_predicate
-- ----------------------------
INSERT INTO `router_predicate` VALUES (1, 1, 1);
INSERT INTO `router_predicate` VALUES (5, 4, 5);
INSERT INTO `router_predicate` VALUES (6, 5, 6);
INSERT INTO `router_predicate` VALUES (7, 6, 7);
INSERT INTO `router_predicate` VALUES (8, 7, 8);
INSERT INTO `router_predicate` VALUES (38, 2, 1);
INSERT INTO `router_predicate` VALUES (39, 2, 2);
INSERT INTO `router_predicate` VALUES (40, 2, 3);
INSERT INTO `router_predicate` VALUES (41, 18, 9);
INSERT INTO `router_predicate` VALUES (42, 19, 10);
INSERT INTO `router_predicate` VALUES (43, 3, 4);
INSERT INTO `router_predicate` VALUES (44, 20, 11);
INSERT INTO `router_predicate` VALUES (45, 21, 12);

SET FOREIGN_KEY_CHECKS = 1;
