# MusicWeb 项目说明

## 1. 快速开始
1. **数据库配置**：
    - 请在本地 MySQL 中运行根目录下的 `musicdb.sql` 文件。
    - **重要**：打开 `src/main/java/com/music/util/DBUtil.java`，修改 `USER` 和 `PASS` 为你自己的数据库账号密码。

2. **Tomcat 配置**：
    - 添加 Tomcat Local Server。
    - Deployment 添加 Artifact: `MusicWeb:war exploded`。
    - **Application Context (上下文路径) 必须设置为**: `/MusicWeb`
    - 这里的路径对应代码中的上传路径，如果填错会导致无法上传。

## 3. 注意事项
- JDK 版本建议：1.8
- Tomcat 版本建议：9.0