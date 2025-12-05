<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.music.bean.Music" %>
<%@ page import="com.music.bean.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Echo · 回声 | deepsuccess.top</title>
    <style>
        /* 全局重置 */
        body { margin: 0; padding: 0; font-family: "Microsoft YaHei", "Segoe UI", sans-serif; background-color: #f4f6f9; color: #333; }
        a { text-decoration: none; transition: 0.3s; }
        ul { list-style: none; padding: 0; margin: 0; }

        /* 主容器 */
        .container {
            width: 1000px;
            margin: 40px auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.08);
            overflow: hidden;
            min-height: 600px;
            display: flex;
            flex-direction: column;
        }

        /* 顶部导航栏 (渐变色) */
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 20px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: white;
        }

        /* 品牌 & 标语 */
        .brand { display: flex; flex-direction: column; }
        .brand h1 { margin: 0; font-size: 24px; font-weight: bold; letter-spacing: 1px; }
        .brand .slogan { font-size: 12px; opacity: 0.9; margin-top: 5px; font-weight: normal; letter-spacing: 2px; }

        /* ✨ 新增：时间显示样式 ✨ */
        .clock-box {
            font-family: 'Consolas', monospace; /* 等宽字体，看起来像电子表 */
            background: rgba(0, 0, 0, 0.2);
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
            color: #fff;
            display: flex;
            align-items: center;
            gap: 8px;
            border: 1px solid rgba(255,255,255,0.1);
        }
        .clock-icon { font-size: 16px; }

        /* 用户面板 & GitHub按钮 */
        .user-panel { font-size: 14px; display: flex; align-items: center; gap: 15px; }
        .user-panel a { color: rgba(255,255,255,0.9); font-weight: 500; }
        .user-panel a:hover { color: white; text-decoration: underline; }

        .btn-github {
            background: #333; color: white !important;
            padding: 6px 12px; border-radius: 4px; font-size: 12px;
            display: flex; align-items: center; gap: 5px;
            border: 1px solid #555; text-decoration: none !important;
        }
        .btn-github:hover { background: black; border-color: white; }

        .btn-upload {
            background: #fff; color: #764ba2 !important;
            padding: 6px 15px; border-radius: 20px; font-weight: bold;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2); text-decoration: none !important;
        }
        .btn-upload:hover { background: #f0f0f0; }

        /* 列表区域 */
        .content-area { padding: 0 20px; flex: 1; }
        .section-title {
            margin: 30px 20px 15px;
            font-size: 18px; color: #444;
            border-left: 4px solid #764ba2; padding-left: 10px; font-weight: bold;
        }

        .music-list { width: 100%; }
        .list-item {
            display: flex; justify-content: space-between; align-items: center;
            padding: 15px 20px; border-bottom: 1px solid #f0f0f0;
        }
        .list-item:hover { background-color: #fdfdfd; transform: translateX(5px); }

        .music-info strong { font-size: 16px; color: #222; }
        .music-info .artist { color: #888; font-size: 13px; margin-left: 8px; }
        .music-info .tag { font-size: 12px; color: #fff; background: #ccc; padding: 1px 5px; border-radius: 3px; margin-left: 8px; }
        .tag-up { background-color: #17a2b8; }

        .actions { display: flex; align-items: center; gap: 10px; }
        .play-data { font-size: 12px; color: #999; margin-right: 10px; }

        .btn-story { color: #6f42c1; background: #f3f0ff; padding: 5px 12px; border-radius: 4px; font-size: 13px; text-decoration: none; }
        .btn-story:hover { background: #e0d4fc; }

        .btn-play {
            color: #fff; background: linear-gradient(90deg, #00c6ff, #0072ff);
            padding: 5px 15px; border-radius: 20px; font-size: 13px;
            box-shadow: 0 2px 5px rgba(0,114,255,0.3); text-decoration: none;
        }
        .btn-play:hover { box-shadow: 0 4px 8px rgba(0,114,255,0.4); }

        .empty-box { text-align: center; padding: 60px; color: #999; }

        /* 页脚 */
        .footer {
            border-top: 1px solid #eee;
            padding: 20px;
            text-align: center;
            font-size: 12px;
            color: #aaa;
            background: #fafafa;
            margin-top: 20px;
        }
        .footer a { color: #aaa; text-decoration: none; }
        .footer a:hover { color: #764ba2; }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <div class="brand">
            <h1>Echo · 回声</h1>
            <span class="slogan">念念不忘，必有回响 | 独立音乐创作平台</span>
        </div>

        <div class="clock-box">
            <span class="clock-icon">🕒</span>
            <span id="currentTime">Loading...</span>
        </div>

        <div class="user-panel">
            <a href="https://github.com/TestDemoW/MusicWeb" target="_blank" class="btn-github">
                ⭐ GitHub
            </a>

            <span style="opacity: 0.3;">|</span>

            <%
                User user = (User)session.getAttribute("user");
                if(user == null) {
            %>
            <a href="login.jsp">登录</a>
            <a href="register.jsp">注册</a>
            <% } else { %>
            <span>你好, <strong><%= user.getUsername() %></strong></span>
            <a href="upload.jsp" class="btn-upload">➕ 发布作品</a>

            <% if("admin".equals(user.getRole())) { %>
            <a href="admin" style="color:#ffcccc;">[管理]</a>
            <% } %>

            <a href="auth?action=logout" style="opacity: 0.7;">退出</a>
            <% } %>
        </div>
    </div>

    <div class="content-area">
        <div class="section-title">🔥 热门创作榜单</div>

        <ul class="music-list">
            <%
                List<Music> list = (List<Music>)request.getAttribute("list");
                if(list != null && list.size() > 0) {
                    for(Music m : list) {
            %>
            <li class="list-item">
                <div class="music-info">
                    <strong><%= m.getTitle() %></strong>
                    <span class="artist"><%= m.getArtist() %></span>
                    <span class="tag tag-up">UP: <%= m.getUploaderName() == null ? "System" : m.getUploaderName() %></span>
                </div>

                <div class="actions">
                    <span class="play-data">👂 <%= m.getPlayCount() %> 次收听</span>
                    <a href="article?musicId=<%= m.getId() %>" class="btn-story">📖 创作手记</a>
                    <a href="play?id=<%= m.getId() %>" class="btn-play">▶ Play</a>
                </div>
            </li>
            <%
                }
            } else {
            %>
            <div class="empty-box">
                <h3>🎼 还没有回声...</h3>
                <p>期待你发布第一首原创音乐，点亮这个社区。</p>
            </div>
            <% } %>
        </ul>
    </div>

    <div class="footer">
        <p>&copy; 2025 deepsuccess.top | Echo Music Community. All Rights Reserved.</p>
        <p>
            <a href="https://beian.miit.gov.cn/" target="_blank">京ICP备88888888号-1</a>
            &nbsp;|&nbsp;
            <a href="#">公网安备 1101080202xxxx号</a>
        </p>
    </div>
</div>

<script>
    function updateTime() {
        var now = new Date();
        // 格式化时间：YYYY-MM-DD HH:mm:ss
        var timeStr = now.getFullYear() + "-" +
            String(now.getMonth() + 1).padStart(2, '0') + "-" +
            String(now.getDate()).padStart(2, '0') + " " +
            String(now.getHours()).padStart(2, '0') + ":" +
            String(now.getMinutes()).padStart(2, '0') + ":" +
            String(now.getSeconds()).padStart(2, '0');

        document.getElementById('currentTime').innerText = timeStr;
    }

    // 立即执行一次，防止页面刚加载时闪烁
    updateTime();
    // 每秒刷新一次
    setInterval(updateTime, 1000);
</script>
<jsp:include page="chatbot.jsp" />
</body>
</html>


