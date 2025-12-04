<%--
  Created by IntelliJ IDEA.
  User: w
  Date: 2025/12/4
  Time: 19:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>上传音乐</title>
    <style>
        body { font-family: sans-serif; background: #f0f2f5; display: flex; justify-content: center; padding-top: 50px; }
        .form-box { background: white; padding: 30px; border-radius: 8px; width: 400px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        input[type="text"] { width: 95%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 4px; }
        button { width: 100%; padding: 12px; background: #28a745; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; margin-top: 15px;}
        button:hover { background: #218838; }
        a { display: block; text-align: center; margin-top: 15px; color: #666; text-decoration: none; }
    </style>
</head>
<body>
<div class="form-box">
    <h2 style="text-align:center;">分享你的音乐</h2>
    <form action="upload" method="post" enctype="multipart/form-data">
        <label>歌名</label>
        <input type="text" name="title" required placeholder="请输入歌名">

        <label>歌手</label>
        <input type="text" name="artist" required placeholder="请输入歌手">

        <label style="display:block; margin-top:10px;">选择MP3文件</label>
        <input type="file" name="file" accept=".mp3" required style="margin-top:5px;">

        <button type="submit">开始上传</button>
    </form>
    <a href="index">返回首页</a>
</div>
</body>
</html>