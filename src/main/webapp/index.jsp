<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.music.bean.Music" %>
<%@ page import="com.music.bean.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>校园云音乐 - 首页</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f0f2f5; padding: 20px; }
        .box { width: 900px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }

        /* 头部样式 */
        .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #007bff; padding-bottom: 15px; margin-bottom: 20px; }
        .header h2 { margin: 0; color: #333; }
        .user-panel { font-size: 14px; color: #666; }
        .user-panel a { text-decoration: none; color: #007bff; margin: 0 5px; }
        .user-panel a:hover { text-decoration: underline; }
        .btn-upload { background: #28a745; color: white !important; padding: 5px 10px; border-radius: 4px; text-decoration: none; }
        .btn-admin { color: #dc3545 !important; font-weight: bold; }

        /* 列表样式 */
        .music-list { list-style: none; padding: 0; margin: 0; }
        .list-item { padding: 15px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; transition: 0.2s; }
        /* 斑马纹效果 */
        .list-item:nth-child(odd) { background-color: #fafafa; }
        .list-item:nth-child(even) { background-color: #ffffff; }
        .list-item:hover { background-color: #e6f7ff; transform: translateX(5px); }

        .info { font-size: 16px; color: #333; }
        .artist { color: #888; font-size: 14px; margin-left: 10px; }
        .uploader { font-size: 12px; color: #aaa; margin-left: 10px; background: #eee; padding: 2px 5px; border-radius: 3px; }
        .actions { display: flex; align-items: center; gap: 15px; }
        .play-count { color: #999; font-size: 12px; }
        .btn-play { background: #007bff; color: white; padding: 5px 15px; text-decoration: none; border-radius: 20px; font-size: 13px; }
        .btn-play:hover { background: #0056b3; }
    </style>
</head>
<body>
<div class="box">
    <div class="header">
        <h2>🎵 校园云音乐热歌榜</h2>

        <div class="user-panel">
            <%
                // 获取当前 Session 中的用户
                User user = (User)session.getAttribute("user");

                if(user == null) {
                    // ==== 未登录状态 ====
            %>
            <span>游客你好，请</span>
            <a href="login.jsp">登录</a> |
            <a href="register.jsp">注册</a>
            <%
            } else {
                // ==== 已登录状态 ====
            %>
            <span>欢迎, <strong><%= user.getUsername() %></strong></span>

            <a href="upload.jsp" class="btn-upload">📤 上传新歌</a>

            <% if("admin".equals(user.getRole())) { %>
            <a href="admin" class="btn-admin">🛠️ 管理后台</a>
            <% } %>

            <a href="auth?action=logout" style="color:#666;">[注销]</a>
            <% } %>
        </div>
    </div>

    <ul class="music-list">
        <%
            // 获取 Servlet 传来的 list (已经在DAO层过滤了，只显示 status=1 的已审核歌曲)
            List<Music> list = (List<Music>)request.getAttribute("list");

            if(list != null && list.size() > 0) {
                for(Music m : list) {
        %>
        <li class="list-item">
            <div class="info">
                <strong><%= m.getTitle() %></strong>
                <span class="artist"><%= m.getArtist() %></span>
                <span class="uploader">UP: <%= m.getUploaderName() == null ? "未知" : m.getUploaderName() %></span>
            </div>
            <div class="actions">
                <span class="play-count">🎧 <%= m.getPlayCount() %> 次播放</span>
                <a href="play?id=<%= m.getId() %>" class="btn-play">▶ 立即播放</a>
            </div>
        </li>
        <%
            }
        } else {
        %>
        <div style="text-align:center; padding: 40px; color: #999;">
            <h3>📭 暂无上榜歌曲</h3>
            <p>快去上传第一首音乐吧！(上传后需要管理员审核通过才会显示)</p>
        </div>
        <% } %>
    </ul>
</div>
</body>
</html>