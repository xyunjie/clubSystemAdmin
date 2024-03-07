/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : jzx

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 07/03/2024 16:52:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity`  (
  `id` bigint NOT NULL COMMENT '公告id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告title',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告内容',
  `club_id` bigint NULL DEFAULT NULL COMMENT '公告发布的社团',
  `kind` enum('notice','activity','warning','system_notice') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'notice' COMMENT '类型',
  `sort` int NOT NULL DEFAULT 100 COMMENT '排序',
  `top` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否置顶',
  `status` int NOT NULL DEFAULT 0 COMMENT '审核状态',
  `created_by` bigint NOT NULL COMMENT '公告创建者',
  `views` int NOT NULL DEFAULT 0 COMMENT '点击量',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '社团公告/活动表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_activity
-- ----------------------------
INSERT INTO `t_activity` VALUES (1759213901792260097, 'test', 'test', 1756953185202855937, 'notice', 100, b'0', 1, 1755199590053564417, 0, NULL, NULL, '2024-02-18 21:50:53', '2024-02-18 21:52:36', 1);
INSERT INTO `t_activity` VALUES (1763103817438420993, '测试标题', '测试内容', NULL, 'system_notice', 100, b'0', 0, 1755199590053564417, 0, NULL, NULL, '2024-02-29 15:28:02', '2024-02-29 15:28:02', 0);
INSERT INTO `t_activity` VALUES (1763104393115029505, '测试标题2', '测试内容2', NULL, 'system_notice', 100, b'0', 0, 1755199590053564417, 0, NULL, NULL, '2024-02-29 15:30:19', '2024-02-29 15:30:19', 0);

-- ----------------------------
-- Table structure for t_activity_user_map
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_user_map`;
CREATE TABLE `t_activity_user_map`  (
  `id` bigint NOT NULL COMMENT '活动成员关系',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `user_id` bigint NOT NULL COMMENT '成员ID',
  `status` int NOT NULL COMMENT '审核状态',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动用户映射' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_activity_user_map
-- ----------------------------

-- ----------------------------
-- Table structure for t_club
-- ----------------------------
DROP TABLE IF EXISTS `t_club`;
CREATE TABLE `t_club`  (
  `id` bigint NOT NULL COMMENT '组织ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织描述',
  `money` decimal(5, 2) NOT NULL COMMENT '加入需要的钱',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '社团余额',
  `created_by` bigint NOT NULL COMMENT '组织创建者',
  `status` int NOT NULL DEFAULT 0 COMMENT '组织审核状态',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '组织创建时间',
  `kind` enum('union','club') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'club' COMMENT '组织类型',
  `views` int NOT NULL DEFAULT 0 COMMENT '点击量',
  `appendix` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '附件地址',
  `sort` int NOT NULL DEFAULT 100 COMMENT '排序',
  `top` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否置顶',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `created_by`(`created_by` ASC) USING BTREE,
  CONSTRAINT `t_club_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '社团表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_club
-- ----------------------------
INSERT INTO `t_club` VALUES (1756939006999789569, '测试社团2', '测试社团2', 10.00, 10.00, 1756588473923407874, 1, '2024-02-12 15:11:16', 'club', 0, NULL, 100, b'0', '2024-02-15 15:20:41', 0);
INSERT INTO `t_club` VALUES (1756953185202855937, '测试社团', '测试社团', 10.00, 0.00, 1755199590053564417, 1, '2024-02-12 16:07:37', 'club', 0, 'https://file.xn--8mrz94a38l8mb00n.top/jzx/20240212/31bf360abfd94d6abe6488f07c2928f6/《响应式Web设计》课程设计-徐云杰.pdf', 100, b'0', '2024-02-12 16:35:13', 0);

-- ----------------------------
-- Table structure for t_club_user_map
-- ----------------------------
DROP TABLE IF EXISTS `t_club_user_map`;
CREATE TABLE `t_club_user_map`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `user_id` bigint NOT NULL COMMENT '成员ID',
  `status` int NOT NULL COMMENT '加入状态',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `club_id`(`club_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `t_club_user_map_ibfk_1` FOREIGN KEY (`club_id`) REFERENCES `t_club` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `t_club_user_map_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '社团/角色映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_club_user_map
-- ----------------------------
INSERT INTO `t_club_user_map` VALUES (1756939007020761089, 1756939006999789569, 1756588473923407874, -1, '2024-02-12 15:11:16', '2024-02-12 15:11:16', 0);
INSERT INTO `t_club_user_map` VALUES (1756953185202855938, 1756953185202855937, 1755199590053564417, -1, '2024-02-12 16:07:37', '2024-02-12 16:07:37', 0);
INSERT INTO `t_club_user_map` VALUES (1758028538541367298, 1756939006999789569, 1755199590053564417, 1, '2024-02-15 15:20:41', '2024-02-15 16:57:11', 0);

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`  (
  `id` bigint NOT NULL COMMENT '字典ID',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '字典父ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `grade` bigint NULL DEFAULT NULL COMMENT '年级（用于班级字典）',
  `is_grade` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是班级',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典描述',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `grade`(`grade` ASC) USING BTREE,
  CONSTRAINT `t_dict_ibfk_1` FOREIGN KEY (`grade`) REFERENCES `t_dict` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_dict
-- ----------------------------
INSERT INTO `t_dict` VALUES (1755850881535848450, NULL, '数据智能学院', NULL, b'0', 'XXXXXX', '2024-02-09 15:07:27', '2024-02-11 16:29:54', 0);
INSERT INTO `t_dict` VALUES (1755851112209985538, 1755850881535848450, '软件工程', NULL, b'0', '软件工程', '2024-02-09 15:08:22', '2024-02-29 07:02:08', 0);
INSERT INTO `t_dict` VALUES (1755872092500279297, NULL, '2020级', NULL, b'1', '2020级', '2024-02-09 16:31:44', '2024-02-28 14:49:14', 0);
INSERT INTO `t_dict` VALUES (1756601791987912706, NULL, '海洋工程学院', NULL, b'0', '海洋工程学院', '2024-02-11 16:51:18', '2024-02-11 16:51:18', 0);
INSERT INTO `t_dict` VALUES (1762762830396555266, 1755851112209985538, '20级软件本3班', 1755872092500279297, b'0', '323232', '2024-02-28 16:53:04', '2024-02-29 14:57:06', 0);
INSERT INTO `t_dict` VALUES (1762763117802848258, 1755850881535848450, '人工智能', NULL, b'0', '3232', '2024-02-28 16:54:13', '2024-02-29 14:48:11', 0);
INSERT INTO `t_dict` VALUES (1762763653990088706, 1756601791987912706, '物联网工程', NULL, b'0', 'xxx', '2024-02-28 16:56:20', '2024-02-29 14:47:49', 0);
INSERT INTO `t_dict` VALUES (1763096430581223426, 1755851112209985538, '20级软件本1班', 1755872092500279297, b'0', '', '2024-02-29 14:58:40', '2024-02-29 14:58:40', 0);

-- ----------------------------
-- Table structure for t_exciting_moments
-- ----------------------------
DROP TABLE IF EXISTS `t_exciting_moments`;
CREATE TABLE `t_exciting_moments`  (
  `id` bigint NOT NULL COMMENT '活动瞬间ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片链接',
  `created_ by` bigint NOT NULL COMMENT '创建者',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动精彩瞬间' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_exciting_moments
-- ----------------------------

-- ----------------------------
-- Table structure for t_finance
-- ----------------------------
DROP TABLE IF EXISTS `t_finance`;
CREATE TABLE `t_finance`  (
  `id` bigint NOT NULL COMMENT '财务ID',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `balance` decimal(10, 2) NOT NULL COMMENT '余额',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '财务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_finance
-- ----------------------------

-- ----------------------------
-- Table structure for t_finance_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_finance_detail`;
CREATE TABLE `t_finance_detail`  (
  `id` bigint NOT NULL COMMENT '资产明细',
  `club_id` bigint NOT NULL COMMENT '社团ID',
  `club_user_map_id` bigint NULL DEFAULT NULL COMMENT '加入社团社费',
  `created_by` bigint NOT NULL COMMENT '操作人',
  `amount` decimal(10, 2) NOT NULL COMMENT '交易金额',
  `balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '交易后余额',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '财务明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_finance_detail
-- ----------------------------
INSERT INTO `t_finance_detail` VALUES (1758028538541367299, 1756939006999789569, 1758028538541367298, 1755199590053564417, 10.00, 20.00, 2, '加入社团', '2024-02-15 15:20:41', '2024-02-15 16:57:11', 0);

-- ----------------------------
-- Table structure for t_system
-- ----------------------------
DROP TABLE IF EXISTS `t_system`;
CREATE TABLE `t_system`  (
  `id` bigint NOT NULL COMMENT '系统设置ID',
  `about` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关于我们',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统简介',
  `club_template` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新增社团模板文件',
  `updated_by` bigint NOT NULL COMMENT '更新人',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_system
-- ----------------------------
INSERT INTO `t_system` VALUES (1763391654255767554, '关于我们', '系统简介', '', 1755199590053564417, '2024-03-01 10:31:47', '2024-03-01 03:28:31', 0);

-- ----------------------------
-- Table structure for t_token
-- ----------------------------
DROP TABLE IF EXISTS `t_token`;
CREATE TABLE `t_token`  (
  `id` bigint NOT NULL COMMENT 'tokenID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'token值',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'token表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_token
-- ----------------------------
INSERT INTO `t_token` VALUES (1755203611060633602, 1755199590053564417, 'c169b19c-14fb-4f29-9f66-ec14f14def92', '2024-03-13 13:53:15', '2024-02-07 20:15:26', '2024-03-06 13:53:15', 0);
INSERT INTO `t_token` VALUES (1756596662358249473, 1756588473923407874, '8bb92342-e483-44da-9e1a-9fee3a725c03', '2024-02-11 16:31:02', '2024-02-11 16:30:55', '2024-02-11 16:31:02', 0);
INSERT INTO `t_token` VALUES (1764460040045813762, 1763098109519089666, '00a69fbc-048e-46da-9e96-8d7f2a40823b', '2024-03-11 10:12:09', '2024-03-04 09:17:10', '2024-03-04 10:12:09', 0);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL COMMENT '成员ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成员姓名',
  `student_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号/登录账号',
  `password` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `sex` bit(1) NOT NULL DEFAULT b'1' COMMENT '性别',
  `user_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '个人介绍',
  `qq` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'qq',
  `we_chat` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信账号',
  `mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `college` bigint NULL DEFAULT NULL COMMENT '学院',
  `major` bigint NULL DEFAULT NULL COMMENT '专业',
  `clazz` bigint NULL DEFAULT NULL COMMENT '班级',
  `grade` bigint NULL DEFAULT NULL COMMENT '入学年级',
  `status` int NOT NULL COMMENT '状态',
  `role` enum('user','admin','clubAdmin') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'user' COMMENT '角色',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `student_id`(`student_id` ASC) USING BTREE,
  INDEX `college`(`college` ASC) USING BTREE,
  INDEX `major`(`major` ASC) USING BTREE,
  INDEX `clazz`(`clazz` ASC) USING BTREE,
  INDEX `grade`(`grade` ASC) USING BTREE,
  CONSTRAINT `t_user_ibfk_1` FOREIGN KEY (`college`) REFERENCES `t_dict` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `t_user_ibfk_2` FOREIGN KEY (`major`) REFERENCES `t_dict` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `t_user_ibfk_3` FOREIGN KEY (`clazz`) REFERENCES `t_dict` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `t_user_ibfk_4` FOREIGN KEY (`grade`) REFERENCES `t_dict` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1755199590053564417, 'admin', 'admin', '$2a$10$uUGFEk/3Yex3r.dm9st8Ae6WQgqmwnDCogb3gdxP054UfBaX4xbJu', '', NULL, b'1', '超级管理员账号', NULL, NULL, NULL, 1755850881535848450, 1755851112209985538, NULL, NULL, 0, 'admin', '2024-02-07 19:59:27', '2024-02-11 23:34:50', 0);
INSERT INTO `t_user` VALUES (1756588473923407874, '蒋子璇', '202012000994', '$2a$10$SGOvyIdZNvNE2pBmndDu3OHFKshX3zlTevYf3yPcIP3gnpSDz/7Gq', '', '18006309924', b'1', NULL, '', '', '2292240763@qq.com', 1755850881535848450, 1755851112209985538, NULL, 1755872092500279297, 0, 'user', '2024-02-11 15:58:23', '2024-02-11 20:33:53', 0);
INSERT INTO `t_user` VALUES (1763098109519089666, '张三', '202012000111', '$2a$10$OIn8Pwm.8/tOhOdP44zWbuKnyWyWJH9LhX8/rc7e7AAmWax49emsO', NULL, '18345678910', b'1', NULL, '123', '123', '123@qq.com', 1763096430581223426, NULL, NULL, 1755872092500279297, 0, 'user', '2024-02-29 15:05:21', '2024-03-04 01:16:57', 0);

SET FOREIGN_KEY_CHECKS = 1;
