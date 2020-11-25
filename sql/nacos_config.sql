/*
 Navicat Premium Data Transfer

 Source Server         : mysql8
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : 127.0.0.1:3307
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 05/11/2020 15:04:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (4, 'application-dev.yml', 'DEFAULT_GROUP', '# 加解密根密码\njasypt:\n  encryptor:\n    password: #根密码\n\n# Spring 相关\nspring:\n  redis:\n    password:\n    host: eladmin-cloud-redis\n  cloud:\n    sentinel:\n      eager: true\n      transport:\n        dashboard: eladmin-cloud-sentinel:5003\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: false\n    response:\n      enabled: false\n#请求处理的超时时间\nribbon:\n  ReadTimeout: 10000\n  ConnectTimeout: 10000\n\n# mybaits-plus配置\nmybatis-plus:\n  mapper-locations: classpath:/mapper/*Mapper.xml\n  \n\n# spring security 配置\nsecurity:\n  oauth2:\n    resource:\n      loadBalanced: true\n      token-info-uri: http://auth-server:7070/oauth/check_token\n    # 通用放行URL，服务个性化，请在对应配置文件覆盖\n    ignore:\n      urls:\n        - /v2/api-docs\n        - /actuator/**\n# swagger 配置\nswagger:\n  title: El-Admin-cloud Swagger API\n  license: Powered By eladmin-cloud\n  licenseUrl: https://eladmin-cloud.com\n  terms-of-service-url: https://eladmin-cloud.com\n  contact:\n    email: eladmincloud@gmail.com\n    url: https://eladmincloud.com\n  authorization:\n    name: eladminCloud OAuth\n    auth-regex: ^.*$\n    authorization-scope-list:\n      - scope: server\n        description: server all\n    token-url-list:\n      - http://${GATEWAY_HOST:127.0.0.1}:${GATEWAY-PORT:80}/auth/oauth/token', '544ebfe1f2bd8e269a5c580c7b0fd335', '2020-08-30 08:37:29', '2020-09-02 13:17:16', NULL, '127.0.0.1', '', '', '共享配置', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (5, 'gateway-server-dev.yml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:p6spy:mysql://127.0.0.1:3306/gateway?useUnicode=true&characterEncoding=UTF-8\n    username: ENC(Mq+1AQuWzTtgfmvXYajdWA==)\n    password: ENC(iKC52MvAJd794FioaKeLWGI5Gdh9p7sv)\n    driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n    hikari:\n      max-lifetime: 10000\n  redis:\n    host: 127.0.0.1\nsecurity:\n  decode:\n    private-key: MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAwf6IqDZH02M0SPLP50HkG7kKxeAWxxaUwOqJQAggnYzMKwcywPaog9MSxB35TScjmhUNKwAcX0ttgQgOiGrI0QIDAQABAkBwCSkxRI/8tClpC9ooK7SqkhZ3qvBcsFzegVUcxtTXR4sUKBsz8R1aIm2LV/ZMeoZ1xVbjbiWYiVvcGf14PCjxAiEA+vrOWSAx/SAnnc0pgK1tZz4gErYFHYWUjmi+yYPfs+UCIQDF3+wEwomPMkvMvuoEU1gTKauIjrdWfXbbYOOUefUKfQIhAM3FfBEOmG2UQ2bZosFhb2VKRjmRCridoKLxthq6CFsRAiAYnb/F49GrH82wPfvs59VoJOHzDhYey2Ly7b3CJd3nHQIhAJV0MKpSnX70d3DerJrDHD3fsUxG6t7dtz5WgzDPOlct\n# 不校验验证码终端\nignore:\n  clients:\n    - test\naliyun:\n  captcha:\n    region-id: cn-hangzhou\n    accessKey-id: \n    access-key-secret: \n    app-key: ', '', '2020-08-30 08:38:36', '2020-10-27 10:38:53', NULL, '127.0.0.1', '', '', '网关配置', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (6, 'auth-server-dev.yml', 'DEFAULT_GROUP', 'spring:\r\n  redis:\r\n    host: 127.0.0.1\r\n  datasource:\r\n    url: jdbc:p6spy:mysql://127.0.0.1:3306/authorization_server?useUnicode=true&characterEncoding=UTF-8\r\n    username: ENC(Mq+1AQuWzTtgfmvXYajdWA==)\r\n    password: ENC(iKC52MvAJd794FioaKeLWGI5Gdh9p7sv)\r\n    driver-class-name: com.p6spy.engine.spy.P6SpyDriver\r\n    hikari:\r\n      max-lifetime: 10000\r\n\r\n', '01731fbf1f498553afe5fa598edf3c1a', '2020-08-30 08:41:10', '2020-08-30 08:41:10', NULL, '0:0:0:0:0:0:0:1', '', '', '认证服务配置', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (7, 'gen-server-dev.yml', 'DEFAULT_GROUP', 'spring:\r\n  datasource:\r\n    url: jdbc:p6spy:mysql://127.0.0.1:3306/eladmin_gen?useUnicode=true&characterEncoding=UTF-8\r\n    driver-class-name: com.p6spy.engine.spy.P6SpyDriver\r\n    username: ENC(Mq+1AQuWzTtgfmvXYajdWA==)\r\n    password: ENC(iKC52MvAJd794FioaKeLWGI5Gdh9p7sv)\r\ngenerator:\r\n  enabled: true', 'eaf7be6be22a8db6c46cabd509fe2f51', '2020-08-30 08:45:53', '2020-08-30 08:45:53', NULL, '0:0:0:0:0:0:0:1', '', '', '代码生成服务配置', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (8, 'log-server-dev.yml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:p6spy:mysql://127.0.0.1:3306/eladmin?useUnicode=true&characterEncoding=UTF-8\n    username: ENC(Mq+1AQuWzTtgfmvXYajdWA==)\n    password: ENC(iKC52MvAJd794FioaKeLWGI5Gdh9p7sv)\n    driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n    hikari:\n      max-lifetime: 10000\n  redis:\n    host: 127.0.0.1\nsecurity:\n  login:\n    cache-enable: true\n    single-login: true\n  oauth2:\n    client:\n      client-id: admin-web\n      client-secret: 123456\n', '352b6f90a001a5bf6a8dc3d879e3ec1b', '2020-08-30 08:48:37', '2020-08-30 08:49:36', NULL, '0:0:0:0:0:0:0:1', '', '', '日志服务配置', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (12, 'system-server-dev.yml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:p6spy:mysql://127.0.0.1:3306/eladmin?useUnicode=true&characterEncoding=UTF-8\n    username: ENC(Mq+1AQuWzTtgfmvXYajdWA==)\n    password: ENC(iKC52MvAJd794FioaKeLWGI5Gdh9p7sv)\n    driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n    hikari:\n      max-lifetime: 10000\n  redis:\n    host: 127.0.0.1\n  rabbitmq:\n    host: 127.0.0.1\n    username: eladmin\n    password: eladmin\n    virtual-host: /eladmin\nsecurity:\n  login:\n    cache-enable: true\n    single-login: true\n  oauth2:\n    client:\n      client-id: system-client\n      client-secret: 123456\neladmin:\n  mq:\n    exchange-name: ELADMIN.MQ.EXCHANGE\n    exchange-auto-delete: false\n    exchange-durable: true\n    queue-durable: true\n    exchange-model: direct\n    queue-model: default', '16ff8729233d26c8d138f54fcf1b20fb', '2020-08-30 08:52:36', '2020-09-12 09:04:04', NULL, '0:0:0:0:0:0:0:1', '', '', '系统服务配置', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (24, 'job-server-dev.yml', 'DEFAULT_GROUP', 'xxl:\r\n  job:\r\n    admin-addresses: http://127.0.0.1:8088/xxl-job-admin\r\n    access-token:\r\n    app-name: xxl-job-executor\r\n    address:\r\n    ip:\r\n    port: 9999\r\n    log-path: /data/applogs/xxl-job/jobhandler\r\n    log-retention-days: 30', '5cd30377e5c8ec52228d111b72b03c44', '2020-09-02 13:27:11', '2020-09-02 13:27:11', NULL, '127.0.0.1', '', '', 'xxl-job定时任务', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (25, 'mnt-server-dev.yml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:p6spy:mysql://127.0.0.1:3306/eladmin_mnt?useUnicode=true&characterEncoding=UTF-8\n    username: ENC(Mq+1AQuWzTtgfmvXYajdWA==)\n    password: ENC(iKC52MvAJd794FioaKeLWGI5Gdh9p7sv)\n    driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n    hikari:\n      max-lifetime: 1900000\n  redis:\n    host: 127.0.0.1\nsecurity:\n  login:\n    cache-enable: true\n    single-login: true\n  oauth2:\n    client:\n      client-id: admin-web\n      client-secret: 123456\n', 'b81a225c56ef2ed6845ab08d4a2770ce', '2020-09-04 09:47:32', '2020-09-04 10:18:57', NULL, '127.0.0.1', '', '', '运维管理配置文件', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (29, 'tool-server-dev.yml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:p6spy:mysql://127.0.0.1:3306/eladmin?useUnicode=true&characterEncoding=UTF-8\n    username: ENC(Mq+1AQuWzTtgfmvXYajdWA==)\n    password: ENC(iKC52MvAJd794FioaKeLWGI5Gdh9p7sv)\n    driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n    hikari:\n      max-lifetime: 10000\n  redis:\n    host: 127.0.0.1\n  rabbitmq:\n    host: 127.0.0.1\n    username: eladmin\n    password: eladmin\n    virtual-host: /eladmin\n    listener:\n      simple:\n        acknowledge-mode: manual\nsecurity:\n  login:\n    cache-enable: true\n    single-login: true\n  oauth2:\n    client:\n      client-id: admin-web\n      client-secret: 123456', 'e28be8abe0cc5bef97c9b7130f7bff50', '2020-09-12 08:41:27', '2020-09-12 09:31:33', NULL, '0:0:0:0:0:0:0:1', '', '', '工具服务配置', '', '', 'yaml', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(0) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(0) UNSIGNED NOT NULL,
  `nid` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (5, 36, 'gateway-server-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  datasource:\n    url: jdbc:p6spy:mysql://127.0.0.1:3306/gateway?useUnicode=true&characterEncoding=UTF-8\n    username: ENC(Mq+1AQuWzTtgfmvXYajdWA==)\n    password: ENC(iKC52MvAJd794FioaKeLWGI5Gdh9p7sv)\n    driver-class-name: com.p6spy.engine.spy.P6SpyDriver\n    hikari:\n      max-lifetime: 10000\n  redis:\n    host: 127.0.0.1\nsecurity:\n  decode:\n    private-key: MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAwf6IqDZH02M0SPLP50HkG7kKxeAWxxaUwOqJQAggnYzMKwcywPaog9MSxB35TScjmhUNKwAcX0ttgQgOiGrI0QIDAQABAkBwCSkxRI/8tClpC9ooK7SqkhZ3qvBcsFzegVUcxtTXR4sUKBsz8R1aIm2LV/ZMeoZ1xVbjbiWYiVvcGf14PCjxAiEA+vrOWSAx/SAnnc0pgK1tZz4gErYFHYWUjmi+yYPfs+UCIQDF3+wEwomPMkvMvuoEU1gTKauIjrdWfXbbYOOUefUKfQIhAM3FfBEOmG2UQ2bZosFhb2VKRjmRCridoKLxthq6CFsRAiAYnb/F49GrH82wPfvs59VoJOHzDhYey2Ly7b3CJd3nHQIhAJV0MKpSnX70d3DerJrDHD3fsUxG6t7dtz5WgzDPOlct\n# 不校验验证码终端\nignore:\n  clients:\n    - test', 'c35a3a129a1e1e8729ba31360b61702c', '2020-10-27 10:38:52', '2020-10-27 10:38:53', NULL, '111.60.28.180', 'U', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
