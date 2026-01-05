-- 数据库初始化脚本 (MySQL)
-- 遵循 MyBatis Plus 命名规范 (下划线命名)

CREATE DATABASE IF NOT EXISTS `student_management` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `student_management`;

-- 1. 学生表
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `sno` VARCHAR(50) NOT NULL UNIQUE COMMENT '学号',
  `sname` VARCHAR(100) NOT NULL COMMENT '姓名',
  `ssex` VARCHAR(10) DEFAULT '未知' COMMENT '性别',
  `score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '成绩',
  `sbirthday` DATE COMMENT '生日',
  `shouji` VARCHAR(20) COMMENT '手机号',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 2. 用户表 (管理员)
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `username` VARCHAR(100) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `phone` VARCHAR(20) COMMENT '手机号',
  `sex` VARCHAR(10) DEFAULT '未知' COMMENT '性别',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 初始管理员数据 (密码示例: 123456)
INSERT INTO `user` (`username`, `password`, `phone`, `sex`) VALUES ('admin', '123456', '13800138000', '男');
