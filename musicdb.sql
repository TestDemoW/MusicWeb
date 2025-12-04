CREATE DATABASE IF NOT EXISTS musicdb DEFAULT CHARSET utf8mb4;
USE musicdb;

CREATE TABLE music (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(100),
                       artist VARCHAR(50),
                       file_path VARCHAR(255),
                       play_count INT DEFAULT 0,
                       upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入一条测试数据
INSERT INTO music (title, artist, file_path, play_count) VALUES ('测试歌曲', 'System', 'test.mp3', 0);