/*
 Navicat Premium Dump SQL

 Source Server         : mysql8.4
 Source Server Type    : MySQL
 Source Server Version : 80403 (8.4.3)
 Source Host           : localhost:3306
 Source Schema         : database_course_design

 Target Server Type    : MySQL
 Target Server Version : 80403 (8.4.3)
 File Encoding         : 65001

 Date: 17/01/2025 20:21:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apprisement_code
-- ----------------------------
DROP TABLE IF EXISTS `apprisement_code`;
CREATE TABLE `apprisement_code`  (
  `code` int NOT NULL,
  `description` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of apprisement_code
-- ----------------------------
INSERT INTO `apprisement_code` VALUES (0, '未考核');
INSERT INTO `apprisement_code` VALUES (1, '不及格');
INSERT INTO `apprisement_code` VALUES (2, '及格');
INSERT INTO `apprisement_code` VALUES (3, '良好');
INSERT INTO `apprisement_code` VALUES (4, '优秀');

-- ----------------------------
-- Table structure for authority_code
-- ----------------------------
DROP TABLE IF EXISTS `authority_code`;
CREATE TABLE `authority_code`  (
  `code` int NOT NULL,
  `description` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority_code
-- ----------------------------
INSERT INTO `authority_code` VALUES (0, '管理员');
INSERT INTO `authority_code` VALUES (1, '普通用户');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `teacher` int NULL DEFAULT NULL,
  `intro` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `book` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `classroom` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `number` int NULL DEFAULT NULL,
  `classtime` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `state` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `state`(`state` ASC) USING BTREE,
  INDEX `teacher`(`teacher` ASC) USING BTREE,
  CONSTRAINT `state` FOREIGN KEY (`state`) REFERENCES `course_state` (`code`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `teacher` FOREIGN KEY (`teacher`) REFERENCES `person` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `number` CHECK (`number` > 0)
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (11, 'C语言程序设计', NULL, '计算机基础学科', 'C语言程序设计', '一教502', 2, '24-25-1', 0);
INSERT INTO `course` VALUES (12, '数据结构', NULL, '数据结构与算法', '数据结构', '一教408', 30, '24-25-1', 0);
INSERT INTO `course` VALUES (13, '数据结构课程设计', NULL, '数据结构课程设计', '数据结构', '逸夫楼533', 30, '24-25-1', 0);
INSERT INTO `course` VALUES (14, '大学英语1', NULL, '大学英语', '视听说教程2', '一教101', 30, '24-25-1', 0);
INSERT INTO `course` VALUES (15, '普通物理学B2', NULL, '大学物理', '大学物理下册', '一教108', 30, '24-25-1', 1);

-- ----------------------------
-- Table structure for course_select
-- ----------------------------
DROP TABLE IF EXISTS `course_select`;
CREATE TABLE `course_select`  (
  `course` int NOT NULL,
  `person` int NOT NULL,
  PRIMARY KEY (`course`, `person`) USING BTREE,
  INDEX `courseselect_person`(`person` ASC) USING BTREE,
  CONSTRAINT `courseselect_course` FOREIGN KEY (`course`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `courseselect_person` FOREIGN KEY (`person`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_select
-- ----------------------------

-- ----------------------------
-- Table structure for course_state
-- ----------------------------
DROP TABLE IF EXISTS `course_state`;
CREATE TABLE `course_state`  (
  `code` int NOT NULL,
  `description` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_state
-- ----------------------------
INSERT INTO `course_state` VALUES (0, '选课中');
INSERT INTO `course_state` VALUES (1, '进行中');
INSERT INTO `course_state` VALUES (2, '已结束');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `manager` int NULL DEFAULT NULL,
  `intro` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `manager`(`manager` ASC) USING BTREE,
  CONSTRAINT `manager` FOREIGN KEY (`manager`) REFERENCES `person` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '计算机学科教学部门', NULL, '计算机');
INSERT INTO `department` VALUES (4, '人文学院', NULL, '人文学院');
INSERT INTO `department` VALUES (5, '计算机学科研究部门', NULL, '计算机研究部门');
INSERT INTO `department` VALUES (6, '数学学院', NULL, '数学学院');
INSERT INTO `department` VALUES (7, '马克思主义学院', NULL, '马院');
INSERT INTO `department` VALUES (8, '数学学院（软件方向）', NULL, '');
INSERT INTO `department` VALUES (9, '物理与电子信息学院', NULL, '物院');
INSERT INTO `department` VALUES (10, '外国语学院', NULL, '');
INSERT INTO `department` VALUES (11, '计算机学院（学生）', NULL, '');
INSERT INTO `department` VALUES (12, '校外部门（软件研发部）', NULL, '');
INSERT INTO `department` VALUES (13, '校外部门（宣传部）', NULL, '');

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL DEFAULT '',
  `passwd` varchar(1024) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `authority` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `sex` varchar(4) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `birthday` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `department` int NULL DEFAULT NULL,
  `job` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `edu_level` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `speciaty` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `tel` varchar(25) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `email` varchar(127) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `state` int NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `department`(`department` ASC) USING BTREE,
  INDEX `person_state`(`state` ASC) USING BTREE,
  INDEX `authority`(`authority` ASC) USING BTREE,
  CONSTRAINT `authority` FOREIGN KEY (`authority`) REFERENCES `authority_code` (`code`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `department` FOREIGN KEY (`department`) REFERENCES `department` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `person_state` FOREIGN KEY (`state`) REFERENCES `person_state` (`code`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES (0, 'admin', '', 0, '超级管理员', '', '', NULL, '', '', '', '', '', '', NULL, '');

-- ----------------------------
-- Table structure for person_state
-- ----------------------------
DROP TABLE IF EXISTS `person_state`;
CREATE TABLE `person_state`  (
  `code` int NOT NULL,
  `description` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of person_state
-- ----------------------------
INSERT INTO `person_state` VALUES (0, '在职');
INSERT INTO `person_state` VALUES (1, '离职');

-- ----------------------------
-- Table structure for training_plan
-- ----------------------------
DROP TABLE IF EXISTS `training_plan`;
CREATE TABLE `training_plan`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `person` int NULL DEFAULT NULL,
  `course` int NULL DEFAULT NULL,
  `score` int NULL DEFAULT NULL,
  `apprisement` int NULL DEFAULT NULL,
  `exam_date` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `person`(`person` ASC) USING BTREE,
  INDEX `course`(`course` ASC) USING BTREE,
  INDEX `apprisement`(`apprisement` ASC) USING BTREE,
  CONSTRAINT `apprisement` FOREIGN KEY (`apprisement`) REFERENCES `apprisement_code` (`code`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `course` FOREIGN KEY (`course`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person` FOREIGN KEY (`person`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `score` CHECK ((`score` >= 0) and (`score` <= 100))
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of training_plan
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
