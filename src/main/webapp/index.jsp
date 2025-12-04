<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.music.bean.Music" %>
<!DOCTYPE html>
<html>
<head>
    <title>校园云音乐</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f0f2f5; padding: 20px; }
        .box { width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #007bff; padding-bottom: 10px; margin-bottom: 15px; }
        .upload-btn { background: #007bff; color: white; padding: 8px 15px; text-decoration: none; border-radius: 4px; font-size: 14px; }
        .list-item { padding: 12px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; }
        .list-item:hover { background: #fafafa; }
        .play-btn { color: #007bff; font-weight: bold; text-decoration: none; }
    </style>
</head>
<body>
<div class="box">
    <div class="header">
        <h2>🎵 热门榜单</h2>
        <a href="upload.jsp" class="upload-btn">📤 上传新歌</a>
    </div>

    <%
        // 接收 Servlet 传过来的 list 数据
        List<Music> list = (List<Music>)request.getAttribute("list");
        if(list != null && list.size() > 0) {
            for(Music m : list) {
    %>
    <div class="list-item">
        <div>
            <span style="font-size:16px; font-weight:bold;"><%= m.getTitle() %></span>
            <span style="color:#666; font-size:14px; margin-left:8px;">- <%= m.getArtist() %></span>
        </div>
        <div>
            <span style="color:#999; font-size:12px; margin-right:15px;">播放: <%= m.getPlayCount() %></span>
            <a href="play?id=<%= m.getId() %>" class="play-btn">▶ 播放</a>
        </div>
    </div>
    <%
        }
    } else {
    %>
    <p style="text-align:center; color:#999;">暂无歌曲，快去上传第一首吧！</p>
    <% } %>
</div>
</body>
</html>