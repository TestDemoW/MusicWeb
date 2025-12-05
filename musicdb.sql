/*
 * Echo · 回声 (MusicWeb) 数据库完整脚本 - Version 3.0
 * 包含：用户、音乐(审核)、评论、弹幕、文章(创作手记)
 */

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS musicdb DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE musicdb;

-- 2. 清理旧表 (注意外键依赖顺序)
DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS danmaku;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS music;
DROP TABLE IF EXISTS users;

-- 3. 用户表
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
                       password VARCHAR(50) NOT NULL COMMENT '密码',
                       role VARCHAR(20) DEFAULT 'user' COMMENT '角色: user/admin'
);
-- 默认管理员: admin / 123456
INSERT INTO users (username, password, role) VALUES ('admin', '123456', 'admin');

-- 4. 音乐表
CREATE TABLE music (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(100) NOT NULL,
                       artist VARCHAR(50) NOT NULL,
                       file_path VARCHAR(255) NOT NULL,
                       play_count INT DEFAULT 0,
                       upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       status INT DEFAULT 0 COMMENT '0:待审核, 1:已通过',
                       uploader_name VARCHAR(50) DEFAULT 'System'
);
-- 插入测试数据
INSERT INTO music (title, artist, file_path, play_count, status, uploader_name)
VALUES ('测试歌曲', 'System', 'test.mp3', 100, 1, 'admin');

-- 5. 评论表
CREATE TABLE comments (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          music_id INT NOT NULL,
                          username VARCHAR(50) NOT NULL,
                          content TEXT,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (music_id) REFERENCES music(id) ON DELETE CASCADE
);

-- 6. 弹幕表 (WebSocket支持)
CREATE TABLE danmaku (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         music_id INT NOT NULL,
                         content VARCHAR(255) NOT NULL,
                         video_time DECIMAL(10, 2) NOT NULL COMMENT '弹幕出现时间点(秒)',
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (music_id) REFERENCES music(id) ON DELETE CASCADE
);

-- 7. 文章表 (创作手记)
CREATE TABLE articles (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          music_id INT NOT NULL UNIQUE COMMENT '一首歌对应一篇文章',
                          content TEXT COMMENT '手记内容',
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (music_id) REFERENCES music(id) ON DELETE CASCADE
);