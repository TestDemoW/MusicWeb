/*
 * MusicWeb 数据库初始化脚本 (V2.0)
 * 包含：用户表、音乐表(带审核状态)、评论表
 */

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS musicdb DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE musicdb;

-- 2. 清理旧表 (注意顺序：先删有外键依赖的表)
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS music;
DROP TABLE IF EXISTS users;

-- 3. 创建用户表
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
                       password VARCHAR(50) NOT NULL COMMENT '密码',
                       role VARCHAR(20) DEFAULT 'user' COMMENT '角色: user=普通用户, admin=管理员'
);

-- 4. 插入默认超级管理员
-- 账号: admin, 密码: 123456
INSERT INTO users (username, password, role) VALUES ('admin', '123456', 'admin');


-- 5. 创建音乐表 (增加 status 和 uploader_name)
CREATE TABLE music (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(100) NOT NULL,
                       artist VARCHAR(50) NOT NULL,
                       file_path VARCHAR(255) NOT NULL,
                       play_count INT DEFAULT 0,
                       upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       status INT DEFAULT 0 COMMENT '0:待审核, 1:已通过',
                       uploader_name VARCHAR(50) DEFAULT 'System' COMMENT '上传者用户名'
);

-- 插入一条测试数据 (状态为1-已通过，所有人可见)
INSERT INTO music (title, artist, file_path, play_count, status, uploader_name)
VALUES ('系统测试歌曲', 'System', 'test.mp3', 100, 1, 'admin');


-- 6. 创建评论表
CREATE TABLE comments (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          music_id INT NOT NULL COMMENT '关联的音乐ID',
                          username VARCHAR(50) NOT NULL COMMENT '评论人',
                          content TEXT COMMENT '评论内容',
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (music_id) REFERENCES music(id) ON DELETE CASCADE
);