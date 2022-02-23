/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50528
 Source Host           : localhost:3306
 Source Schema         : process

 Target Server Type    : MySQL
 Target Server Version : 50528
 File Encoding         : 65001

 Date: 12/01/2022 17:31:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for easy_message
-- ----------------------------
DROP TABLE IF EXISTS `easy_message`;
CREATE TABLE `easy_message`  (
  `SID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户',
  `TASK_ID` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `CREATED` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATED` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`SID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for easy_process
-- ----------------------------
DROP TABLE IF EXISTS `easy_process`;
CREATE TABLE `easy_process`  (
  `SID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TITLE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `BACK_MODE` int(11) NULL DEFAULT NULL COMMENT '退回模式：1. 全部退回  2.退回上一步',
  `RECEIVE_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果抄送人',
  `CREATED` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATED` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`SID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流程定义' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for easy_process_design
-- ----------------------------
DROP TABLE IF EXISTS `easy_process_design`;
CREATE TABLE `easy_process_design`  (
  `SID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `LABEL` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `TYPE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `X` int(11) NULL DEFAULT NULL COMMENT 'x轴',
  `Y` int(11) NULL DEFAULT NULL COMMENT 'y轴',
  `STAGE_ID` bigint(20) NULL DEFAULT NULL COMMENT '阶段ID',
  `SOURCE` bigint(20) NULL DEFAULT NULL COMMENT '源',
  `TARGET` bigint(20) NULL DEFAULT NULL COMMENT '目标',
  `PROCESS_ID` bigint(20) NULL DEFAULT NULL COMMENT '流程ID',
  `CREATED` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATED` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`SID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '节点设计' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for easy_process_stage
-- ----------------------------
DROP TABLE IF EXISTS `easy_process_stage`;
CREATE TABLE `easy_process_stage`  (
  `SID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TITLE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `DEPARTMENT` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门',
  `USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户',
  `ROLE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色',
  `ORDERED` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `MODE` int(11) NULL DEFAULT NULL COMMENT '模式',
  `FIELD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段',
  `CONDITION` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件',
  `VALUE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件值',
  `PROCESS_ID` bigint(20) NULL DEFAULT NULL COMMENT '流程ID',
  `CREATED` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATED` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`SID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '节点' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for easy_task
-- ----------------------------
DROP TABLE IF EXISTS `easy_task`;
CREATE TABLE `easy_task`  (
  `SID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `PROCESS_ID` bigint(20) NULL DEFAULT NULL COMMENT '流程ID',
  `USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交用户',
  `CURRENT_STAGE` bigint(20) NULL DEFAULT NULL COMMENT '当前阶段',
  `TARGET` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标表',
  `TARGET_KEY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标表主键',
  `TARGET_KEY_VALUE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标表主键值',
  `CLASS_PATH` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类路径',
  `STATUS` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `CREATED` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATED` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`SID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for easy_task_step
-- ----------------------------
DROP TABLE IF EXISTS `easy_task_step`;
CREATE TABLE `easy_task_step`  (
  `SID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TASK_ID` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `STAGE_ID` bigint(20) NULL DEFAULT NULL COMMENT '节点ID',
  `USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批用户',
  `ROLE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批角色',
  `OPINION` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批意见',
  `REASON` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因/理由',
  `STATUS` int(11) NULL DEFAULT NULL COMMENT '是否可用：1. 可用 0. 不可用',
  `CREATED` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATED` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`SID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务步骤详情' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
