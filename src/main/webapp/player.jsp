<%--
  Created by IntelliJ IDEA.
  User: w
  Date: 2025/12/4
  Time: 19:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.music.bean.Music" %>
<!DOCTYPE html>
<html>
<head>
    <title>正在播放</title>
    <style>
        body { background: #222; color: #fff; text-align: center; font-family: sans-serif; padding-top: 80px; }
        .player { width: 500px; margin: 0 auto; background: #333; padding: 40px; border-radius: 15px; }
        h1 { margin: 0; font-size: 28px; }
        h3 { color: #bbb; font-weight: normal; margin-top: 10px; }
        audio { width: 100%; margin-top: 30px; outline: none; }
        .back { display: inline-block; margin-top: 30px; color: #aaa; text-decoration: none; border: 1px solid #555; padding: 8px 20px; border-radius: 20px; transition:0.3s;}
        .back:hover { background: white; color: black; }
    </style>
</head>
<body>
<%
    Music m = (Music)request.getAttribute("m");
    if(m != null) {
%>
<div class="player">
    <h1><%= m.getTitle() %></h1>
    <h3><%= m.getArtist() %></h3>

    <audio controls autoplay>
        <source src="<%= request.getContextPath() %>/<%= m.getFilePath() %>" type="audio/mpeg">
        您的浏览器不支持音频播放。
    </audio>

    <p style="color:#666; font-size:12px; margin-top:20px;">累计播放: <%= m.getPlayCount() %></p>
    <a href="index" class="back">← 返回列表</a>
</div>
<% } else { %>
<h2>找不到该音乐</h2>
<a href="index" style="color:white;">返回</a>
<% } %>
</body>
</html>