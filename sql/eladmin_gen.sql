/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 121.89.178.219:3306
 Source Schema         : eladmin_gen

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 05/11/2020 14:26:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for code_column_config
-- ----------------------------
DROP TABLE IF EXISTS `code_column_config`;
CREATE TABLE `code_column_config`  (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `db_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `table_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `column_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `column_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dict_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `extra` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `form_show` bit(1) NULL DEFAULT NULL,
  `form_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `key_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `list_show` bit(1) NULL DEFAULT NULL,
  `not_null` bit(1) NULL DEFAULT NULL,
  `query_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `date_annotation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`column_id`) USING BTREE,
  INDEX `idx_table_name`(`table_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 180 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成字段信息存储' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of code_column_config
-- ----------------------------
INSERT INTO `code_column_config` VALUES (37, 'eladmin', 'tool_qiniu_content', 'content_id', 'bigint', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', NULL, 'ID,不能为空', NULL);
INSERT INTO `code_column_config` VALUES (38, 'eladmin', 'tool_qiniu_content', 'bucket', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, 'Bucket 识别符', NULL);
INSERT INTO `code_column_config` VALUES (39, 'eladmin', 'tool_qiniu_content', 'name', 'varchar', NULL, '', b'1', NULL, 'UNI', b'1', b'0', NULL, '文件名称', NULL);
INSERT INTO `code_column_config` VALUES (40, 'eladmin', 'tool_qiniu_content', 'size', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '文件大小', NULL);
INSERT INTO `code_column_config` VALUES (41, 'eladmin', 'tool_qiniu_content', 'type', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '文件类型：私有或公开', NULL);
INSERT INTO `code_column_config` VALUES (42, 'eladmin', 'tool_qiniu_content', 'url', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '文件url', NULL);
INSERT INTO `code_column_config` VALUES (43, 'eladmin', 'tool_qiniu_content', 'suffix', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '文件后缀', NULL);
INSERT INTO `code_column_config` VALUES (44, 'eladmin', 'tool_qiniu_content', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '上传或同步的时间', NULL);
INSERT INTO `code_column_config` VALUES (45, 'gateway', 'router', 'id', 'int', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (46, 'gateway', 'router', 'router_id', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', '=', '', NULL);
INSERT INTO `code_column_config` VALUES (47, 'gateway', 'router', 'router_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', 'Like', '', NULL);
INSERT INTO `code_column_config` VALUES (48, 'gateway', 'router', 'router_type', 'tinyint', NULL, '', b'1', '', '', b'1', b'1', '', '', NULL);
INSERT INTO `code_column_config` VALUES (49, 'gateway', 'router', 'router_url', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (50, 'gateway', 'router', 'enable', 'tinyint', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (51, 'gateway', 'router', 'threshold', 'bigint', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (52, 'gateway', 'router', 'interval_sec', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (77, 'gateway', 'filter', 'id', 'bigint', NULL, 'auto_increment', b'0', NULL, 'PRI', b'0', b'1', NULL, '主键', NULL);
INSERT INTO `code_column_config` VALUES (78, 'gateway', 'filter', 'filter_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', 'Like', '过滤器名称', NULL);
INSERT INTO `code_column_config` VALUES (79, 'gateway', 'filter', 'filter_val', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', 'Like', '过滤器的值', NULL);
INSERT INTO `code_column_config` VALUES (80, 'gateway', 'predicate', 'id', 'bigint', NULL, 'auto_increment', b'0', NULL, 'PRI', b'0', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (81, 'gateway', 'predicate', 'predicate_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', 'Like', '', NULL);
INSERT INTO `code_column_config` VALUES (82, 'gateway', 'predicate', 'predicate_val', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', 'Like', '', NULL);
INSERT INTO `code_column_config` VALUES (83, 'eladmin', 'mnt_app', 'app_id', 'bigint', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', NULL, 'ID', NULL);
INSERT INTO `code_column_config` VALUES (84, 'eladmin', 'mnt_app', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '应用名称', NULL);
INSERT INTO `code_column_config` VALUES (85, 'eladmin', 'mnt_app', 'upload_path', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '上传目录', NULL);
INSERT INTO `code_column_config` VALUES (86, 'eladmin', 'mnt_app', 'deploy_path', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部署路径', NULL);
INSERT INTO `code_column_config` VALUES (87, 'eladmin', 'mnt_app', 'backup_path', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备份路径', NULL);
INSERT INTO `code_column_config` VALUES (88, 'eladmin', 'mnt_app', 'port', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '应用端口', NULL);
INSERT INTO `code_column_config` VALUES (89, 'eladmin', 'mnt_app', 'start_script', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '启动脚本', NULL);
INSERT INTO `code_column_config` VALUES (90, 'eladmin', 'mnt_app', 'deploy_script', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部署脚本', NULL);
INSERT INTO `code_column_config` VALUES (91, 'eladmin', 'mnt_app', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建者', NULL);
INSERT INTO `code_column_config` VALUES (92, 'eladmin', 'mnt_app', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新者', NULL);
INSERT INTO `code_column_config` VALUES (93, 'eladmin', 'mnt_app', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建日期', NULL);
INSERT INTO `code_column_config` VALUES (94, 'eladmin', 'mnt_app', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新时间', NULL);
INSERT INTO `code_column_config` VALUES (95, 'eladmin', 'mnt_database', 'db_id', 'varchar', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'ID', NULL);
INSERT INTO `code_column_config` VALUES (96, 'eladmin', 'mnt_database', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '名称', NULL);
INSERT INTO `code_column_config` VALUES (97, 'eladmin', 'mnt_database', 'jdbc_url', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, 'jdbc连接', NULL);
INSERT INTO `code_column_config` VALUES (98, 'eladmin', 'mnt_database', 'user_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '账号', NULL);
INSERT INTO `code_column_config` VALUES (99, 'eladmin', 'mnt_database', 'pwd', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '密码', NULL);
INSERT INTO `code_column_config` VALUES (100, 'eladmin', 'mnt_database', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建者', NULL);
INSERT INTO `code_column_config` VALUES (101, 'eladmin', 'mnt_database', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新者', NULL);
INSERT INTO `code_column_config` VALUES (102, 'eladmin', 'mnt_database', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建时间', NULL);
INSERT INTO `code_column_config` VALUES (103, 'eladmin', 'mnt_database', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新时间', NULL);
INSERT INTO `code_column_config` VALUES (104, 'eladmin', 'mnt_deploy', 'deploy_id', 'bigint', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', NULL, 'ID', NULL);
INSERT INTO `code_column_config` VALUES (105, 'eladmin', 'mnt_deploy', 'app_id', 'bigint', NULL, '', b'1', NULL, 'MUL', b'1', b'0', NULL, '应用编号', NULL);
INSERT INTO `code_column_config` VALUES (106, 'eladmin', 'mnt_deploy', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建者', NULL);
INSERT INTO `code_column_config` VALUES (107, 'eladmin', 'mnt_deploy', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新者', NULL);
INSERT INTO `code_column_config` VALUES (108, 'eladmin', 'mnt_deploy', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (109, 'eladmin', 'mnt_deploy', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新时间', NULL);
INSERT INTO `code_column_config` VALUES (110, 'eladmin', 'mnt_deploy_history', 'history_id', 'varchar', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'ID', NULL);
INSERT INTO `code_column_config` VALUES (111, 'eladmin', 'mnt_deploy_history', 'app_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '应用名称', NULL);
INSERT INTO `code_column_config` VALUES (112, 'eladmin', 'mnt_deploy_history', 'deploy_date', 'datetime', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '部署日期', NULL);
INSERT INTO `code_column_config` VALUES (113, 'eladmin', 'mnt_deploy_history', 'deploy_user', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '部署用户', NULL);
INSERT INTO `code_column_config` VALUES (114, 'eladmin', 'mnt_deploy_history', 'ip', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '服务器IP', NULL);
INSERT INTO `code_column_config` VALUES (115, 'eladmin', 'mnt_deploy_history', 'deploy_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部署编号', NULL);
INSERT INTO `code_column_config` VALUES (116, 'eladmin', 'mnt_deploy_server', 'deploy_id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, '部署ID', NULL);
INSERT INTO `code_column_config` VALUES (117, 'eladmin', 'mnt_deploy_server', 'server_id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, '服务ID', NULL);
INSERT INTO `code_column_config` VALUES (118, 'eladmin', 'mnt_server', 'server_id', 'bigint', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', NULL, 'ID', NULL);
INSERT INTO `code_column_config` VALUES (119, 'eladmin', 'mnt_server', 'account', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '账号', NULL);
INSERT INTO `code_column_config` VALUES (120, 'eladmin', 'mnt_server', 'ip', 'varchar', NULL, '', b'1', NULL, 'MUL', b'1', b'0', NULL, 'IP地址', NULL);
INSERT INTO `code_column_config` VALUES (121, 'eladmin', 'mnt_server', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '名称', NULL);
INSERT INTO `code_column_config` VALUES (122, 'eladmin', 'mnt_server', 'password', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '密码', NULL);
INSERT INTO `code_column_config` VALUES (123, 'eladmin', 'mnt_server', 'port', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '端口', NULL);
INSERT INTO `code_column_config` VALUES (124, 'eladmin', 'mnt_server', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建者', NULL);
INSERT INTO `code_column_config` VALUES (125, 'eladmin', 'mnt_server', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新者', NULL);
INSERT INTO `code_column_config` VALUES (126, 'eladmin', 'mnt_server', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建时间', NULL);
INSERT INTO `code_column_config` VALUES (127, 'eladmin', 'mnt_server', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新时间', NULL);
INSERT INTO `code_column_config` VALUES (128, 'eladmin', 'mnt_server_deploy', 'server_id', 'bigint', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', NULL, 'ID', NULL);
INSERT INTO `code_column_config` VALUES (129, 'eladmin', 'mnt_server_deploy', 'account', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '账号', NULL);
INSERT INTO `code_column_config` VALUES (130, 'eladmin', 'mnt_server_deploy', 'ip', 'varchar', NULL, '', b'1', NULL, 'MUL', b'1', b'0', NULL, 'IP地址', NULL);
INSERT INTO `code_column_config` VALUES (131, 'eladmin', 'mnt_server_deploy', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '名称', NULL);
INSERT INTO `code_column_config` VALUES (132, 'eladmin', 'mnt_server_deploy', 'password', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '密码', NULL);
INSERT INTO `code_column_config` VALUES (133, 'eladmin', 'mnt_server_deploy', 'port', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '端口', NULL);
INSERT INTO `code_column_config` VALUES (134, 'eladmin', 'mnt_server_deploy', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建者', NULL);
INSERT INTO `code_column_config` VALUES (135, 'eladmin', 'mnt_server_deploy', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新者', NULL);
INSERT INTO `code_column_config` VALUES (136, 'eladmin', 'mnt_server_deploy', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建时间', NULL);
INSERT INTO `code_column_config` VALUES (137, 'eladmin', 'mnt_server_deploy', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '更新时间', NULL);
INSERT INTO `code_column_config` VALUES (138, 'mi-community', 'mi_user', 'id', 'bigint', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', NULL, '主键ID', NULL);
INSERT INTO `code_column_config` VALUES (139, 'mi-community', 'mi_user', 'username', 'varchar', NULL, '', b'1', NULL, 'UNI', b'1', b'1', NULL, '昵称', NULL);
INSERT INTO `code_column_config` VALUES (140, 'mi-community', 'mi_user', 'password', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '密码', NULL);
INSERT INTO `code_column_config` VALUES (141, 'mi-community', 'mi_user', 'email', 'varchar', NULL, '', b'1', NULL, 'UNI', b'1', b'0', NULL, '邮件', NULL);
INSERT INTO `code_column_config` VALUES (142, 'mi-community', 'mi_user', 'mobile', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '手机电话', NULL);
INSERT INTO `code_column_config` VALUES (143, 'mi-community', 'mi_user', 'point', 'int', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '积分', NULL);
INSERT INTO `code_column_config` VALUES (144, 'mi-community', 'mi_user', 'sign', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '个性签名', NULL);
INSERT INTO `code_column_config` VALUES (145, 'mi-community', 'mi_user', 'gender', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '性别', NULL);
INSERT INTO `code_column_config` VALUES (146, 'mi-community', 'mi_user', 'wechat', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '微信号', NULL);
INSERT INTO `code_column_config` VALUES (147, 'mi-community', 'mi_user', 'vip_level', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, 'vip等级', NULL);
INSERT INTO `code_column_config` VALUES (148, 'mi-community', 'mi_user', 'birthday', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '生日', NULL);
INSERT INTO `code_column_config` VALUES (149, 'mi-community', 'mi_user', 'avatar', 'varchar', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '头像', NULL);
INSERT INTO `code_column_config` VALUES (150, 'mi-community', 'mi_user', 'post_count', 'int', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '内容数量', NULL);
INSERT INTO `code_column_config` VALUES (151, 'mi-community', 'mi_user', 'comment_count', 'int', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '评论数量', NULL);
INSERT INTO `code_column_config` VALUES (152, 'mi-community', 'mi_user', 'status', 'tinyint', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '状态', NULL);
INSERT INTO `code_column_config` VALUES (153, 'mi-community', 'mi_user', 'lasted', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '最后的登陆时间', NULL);
INSERT INTO `code_column_config` VALUES (154, 'mi-community', 'mi_user', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建日期', NULL);
INSERT INTO `code_column_config` VALUES (155, 'mi-community', 'mi_user', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '最后修改时间', NULL);
INSERT INTO `code_column_config` VALUES (156, 'mi-community', 'mi_user_collection', 'id', 'bigint', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', '=', '', NULL);
INSERT INTO `code_column_config` VALUES (157, 'mi-community', 'mi_user_collection', 'user_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'1', '=', '', NULL);
INSERT INTO `code_column_config` VALUES (158, 'mi-community', 'mi_user_collection', 'post_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'1', '=', '', NULL);
INSERT INTO `code_column_config` VALUES (159, 'mi-community', 'mi_user_collection', 'post_user_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (160, 'mi-community', 'mi_user_collection', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (161, 'mi-community', 'mi_user_collection', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (162, 'mi-community', 'mi_user_action', 'id', 'varchar', NULL, '', b'1', NULL, 'PRI', b'1', b'1', '=', '', NULL);
INSERT INTO `code_column_config` VALUES (163, 'mi-community', 'mi_user_action', 'user_id', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', '=', '用户ID', NULL);
INSERT INTO `code_column_config` VALUES (164, 'mi-community', 'mi_user_action', 'action', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '动作类型', NULL);
INSERT INTO `code_column_config` VALUES (165, 'mi-community', 'mi_user_action', 'point', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '得分', NULL);
INSERT INTO `code_column_config` VALUES (166, 'mi-community', 'mi_user_action', 'post_id', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '关联的帖子ID', NULL);
INSERT INTO `code_column_config` VALUES (167, 'mi-community', 'mi_user_action', 'comment_id', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '关联的评论ID', NULL);
INSERT INTO `code_column_config` VALUES (168, 'mi-community', 'mi_user_action', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (169, 'mi-community', 'mi_user_action', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (170, 'mi-community', 'mi_user_message', 'id', 'bigint', NULL, 'auto_increment', b'1', NULL, 'PRI', b'1', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (171, 'mi-community', 'mi_user_message', 'from_user_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '发送消息的用户ID', NULL);
INSERT INTO `code_column_config` VALUES (172, 'mi-community', 'mi_user_message', 'to_user_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '接收消息的用户ID', NULL);
INSERT INTO `code_column_config` VALUES (173, 'mi-community', 'mi_user_message', 'post_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '消息可能关联的帖子', NULL);
INSERT INTO `code_column_config` VALUES (174, 'mi-community', 'mi_user_message', 'comment_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '消息可能关联的评论', NULL);
INSERT INTO `code_column_config` VALUES (175, 'mi-community', 'mi_user_message', 'content', 'text', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (176, 'mi-community', 'mi_user_message', 'type', 'tinyint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '消息类型', NULL);
INSERT INTO `code_column_config` VALUES (177, 'mi-community', 'mi_user_message', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (178, 'mi-community', 'mi_user_message', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '', NULL);
INSERT INTO `code_column_config` VALUES (179, 'mi-community', 'mi_user_message', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '', NULL);

-- ----------------------------
-- Table structure for code_gen_config
-- ----------------------------
DROP TABLE IF EXISTS `code_gen_config`;
CREATE TABLE `code_gen_config`  (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `db_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `table_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者',
  `cover` bit(1) NULL DEFAULT NULL COMMENT '是否覆盖',
  `module_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `pack` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '至于哪个包下',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端代码生成的路径',
  `api_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端Api文件路径',
  `prefix` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表前缀',
  `api_alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  PRIMARY KEY (`config_id`) USING BTREE,
  INDEX `idx_table_name`(`table_name`(100)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成器配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of code_gen_config
-- ----------------------------
INSERT INTO `code_gen_config` VALUES (1, 'eladmin', 'tool_qiniu_content', 'ab', b'0', 'b', 'c', 'e', 'e\\', NULL, 'd');
INSERT INTO `code_gen_config` VALUES (2, 'gateway', 'router', 'a', b'0', 'b', 'v', 'd', 'd\\', NULL, 'd');
INSERT INTO `code_gen_config` VALUES (3, 'gateway', 'filter', 'Micah', b'0', 'eladmin-gateway', 'org.micah.gateway', 'gateway/filter', 'gateway/filter\\', NULL, 'filter');
INSERT INTO `code_gen_config` VALUES (4, 'gateway', 'predicate', 'micah', b'0', 'eladmin-gateway', 'org.micah.gateway', 'gateway/predicate', 'gateway/predicate\\', NULL, 'predicate');
INSERT INTO `code_gen_config` VALUES (5, 'eladmin', 'mnt_app', 'Micah', b'0', 'eladmin-server-mnt', 'org.micah.mnt', 'mnt/app', 'mnt/app\\', 'mnt_', 'Mnt');
INSERT INTO `code_gen_config` VALUES (6, 'eladmin', 'mnt_database', 'Micah', b'0', 'eladmin-server-mnt', 'org.micah', 'mnt/database/index', 'mnt/database/index\\', 'mnt_', 'DataBase');
INSERT INTO `code_gen_config` VALUES (7, 'eladmin', 'mnt_deploy', 'Micah', b'0', 'eladmin-server-mnt', 'org.micah', '/', '/\\', 'mnt_', 'Deploy');
INSERT INTO `code_gen_config` VALUES (8, 'eladmin', 'mnt_deploy_history', 'Micah', b'0', 'eladmin-server-mnt', 'org.micah', '/', '/\\', 'mnt_', 'deployHistory');
INSERT INTO `code_gen_config` VALUES (9, 'eladmin', 'mnt_deploy_server', 'Micah', b'0', 'eladmin-server-mnt', 'org.micah', '/', '/\\', 'mnt_', 'deployServer');
INSERT INTO `code_gen_config` VALUES (10, 'eladmin', 'mnt_server', 'Micah', b'0', 'eladmin-server-mnt', 'org.micah', '/', '/\\', 'mnt_', 'Server');
INSERT INTO `code_gen_config` VALUES (11, 'eladmin', 'mnt_server_deploy', 'micah', b'0', 'eladmin-server-mnt', 'org.micah', '/', '/\\', 'mnt_', 'serverDeploy');
INSERT INTO `code_gen_config` VALUES (12, 'mi-community', 'mi_user', 'Micah', b'0', 'mi-admin-user', 'org.mi.admin.user', '/mi/user', '/mi/user\\', NULL, 'MiUserApi');
INSERT INTO `code_gen_config` VALUES (13, 'mi-community', 'mi_user_collection', 'Micah', b'0', 'mi-admin-user', 'org.mi.admin.user', '/miuser/userCollection', '/miuser/userCollection\\', NULL, 'MiCollectionApi');
INSERT INTO `code_gen_config` VALUES (14, 'mi-community', 'mi_user_action', 'Micah', b'0', 'mi-admin-user', 'org.mi.admin', '/mi/user/action', '/mi/user/action\\', NULL, 'MiUserAction');

SET FOREIGN_KEY_CHECKS = 1;
