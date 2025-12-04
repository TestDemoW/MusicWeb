<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.music.bean.Music" %>
<%@ page import="com.music.bean.User" %>
<%@ page import="com.music.bean.Comment" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>正在播放 - 校园云音乐</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/aplayer/1.10.1/APlayer.min.css">

    <style>
        body { background: #222; color: #fff; font-family: "Microsoft YaHei", sans-serif; margin: 0; padding-top: 50px; }
        .container { width: 700px; margin: 0 auto; padding-bottom: 50px; }

        /* 播放器主体卡片 */
        .player-card { background: #333; padding: 40px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.5); position: relative;}
        h1 { margin: 0; font-size: 28px; color: #fff; text-align: center;}
        h3 { color: #aaa; font-weight: normal; margin-top: 10px; text-align: center;}
        .uploader-info { font-size: 12px; color: #666; margin-top: 5px; background: #222; display: inline-block; padding: 2px 8px; border-radius: 4px; display: block; width: fit-content; margin: 10px auto;}

        /* APlayer 容器样式 */
        #aplayer { margin-top: 30px; box-shadow: 0 5px 15px rgba(0,0,0,0.3); border-radius: 6px; z-index: 10; position: relative;}

        /* ======= 🚀 弹幕层样式 ======= */
        #danmaku-container {
            position: absolute; /* 绝对定位，覆盖在播放器区域 */
            top: 100px; /* 调整起始高度 */
            left: 0;
            width: 100%;
            height: 250px; /* 弹幕活动区域高度 */
            pointer-events: none; /* 关键：让鼠标点击能穿透弹幕层点到播放器 */
            overflow: hidden;
            z-index: 999;
        }
        .danmaku-item {
            position: absolute;
            color: white;
            font-size: 24px;
            font-weight: bold;
            text-shadow: 2px 2px 4px #000000; /* 黑色描边，防止背景太白看不清 */
            white-space: nowrap;
            animation: move 8s linear forwards; /* 8秒飘过屏幕 */
            font-family: "SimHei", sans-serif;
        }
        @keyframes move {
            from { left: 100%; }
            to { left: -100%; } /* 向左飘动 */
        }

        /* 弹幕输入框区域 */
        .dm-input-box { margin-top: 15px; display: flex; gap: 10px; justify-content: center; }
        .dm-input-box input { flex: 1; padding: 10px; border-radius: 4px; border: 1px solid #555; background: #444; color: white; outline: none;}
        .dm-input-box input:focus { border-color: #007bff; }
        .btn-send { background: #ff0055; color: white; border: none; padding: 0 20px; border-radius: 4px; cursor: pointer; font-weight: bold; transition: 0.2s; }
        .btn-send:hover { background: #d60045; transform: scale(1.05); }

        /* 按钮通用样式 */
        .back-btn { display: inline-block; color: #aaa; text-decoration: none; border: 1px solid #555; padding: 8px 20px; border-radius: 20px; transition:0.3s; font-size: 14px; text-align: center;}
        .back-btn:hover { background: white; color: black; }

        /* 分割线 */
        hr { border: 0; border-top: 1px solid #444; margin: 30px 0; }

        /* ======= 💬 评论区样式 ======= */
        .comment-section { text-align: left; }
        .comment-header { font-size: 18px; margin-bottom: 15px; border-left: 4px solid #007bff; padding-left: 10px; }
        .comment-form textarea { width: 100%; padding: 10px; border-radius: 5px; border: none; resize: vertical; box-sizing: border-box; font-family: inherit; background: #eee; color: #333;}
        .comment-form button { margin-top: 10px; padding: 8px 20px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; float: right; }
        .login-tip { background: #444; padding: 15px; border-radius: 5px; text-align: center; color: #ccc; }
        .login-tip a { color: #007bff; text-decoration: none; }

        .comment-list { margin-top: 50px; clear: both; }
        .comment-item { border-bottom: 1px solid #444; padding: 15px 0; }
        .comment-user { color: #007bff; font-weight: bold; font-size: 14px; }
        .comment-time { float: right; color: #666; font-size: 12px; }
        .comment-content { margin-top: 8px; font-size: 14px; color: #ddd; line-height: 1.5; }
        .no-comment { text-align: center; color: #555; margin-top: 20px; }
    </style>
</head>
<body>
<%
    Music m = (Music)request.getAttribute("m");
    if(m != null) {
%>
<div class="container">
    <div class="player-card">
        <h1><%= m.getTitle() %></h1>
        <h3><%= m.getArtist() %></h3>
        <div class="uploader-info">UP主: <%= m.getUploaderName() == null ? "未知" : m.getUploaderName() %></div>

        <div id="danmaku-container"></div>

        <div id="aplayer"></div>

        <div class="dm-input-box">
            <input type="text" id="dmText" placeholder="🚀 发条弹幕互动一下 (回车发送)" maxlength="50" />
            <button class="btn-send" onclick="sendDanmaku()">发射</button>
        </div>

        <div style="text-align:center; margin-top:30px;">
            <a href="index" class="back-btn">← 返回列表</a>

            <a href="download?id=<%= m.getId() %>" class="back-btn" style="background: #28a745; color: white; border-color: #28a745; margin-left: 15px;">
                ⬇️ 下载文件
            </a>
        </div>

        <hr>

        <div class="comment-section">
            <div class="comment-header">📝 听友评论 (<%= m.getPlayCount() %> 次播放)</div>

            <%
                User user = (User)session.getAttribute("user");
                if(user != null) {
            %>
            <form action="comment" method="post" class="comment-form">
                <input type="hidden" name="musicId" value="<%= m.getId() %>">
                <textarea name="content" rows="3" placeholder="写下你的听歌感受..." required></textarea>
                <button type="submit">发表评论</button>
            </form>
            <% } else { %>
            <div class="login-tip">需要 <a href="login.jsp">登录</a> 后才能发表评论</div>
            <% } %>

            <div class="comment-list">
                <%
                    List<Comment> comments = (List<Comment>)request.getAttribute("commentList");
                    if(comments != null && comments.size() > 0) {
                        for(Comment c : comments) {
                %>
                <div class="comment-item">
                    <div>
                        <span class="comment-user"><%= c.getUsername() %></span>
                        <span class="comment-time"><%= c.getCreateTime() %></span>
                    </div>
                    <div class="comment-content"><%= c.getContent() %></div>
                </div>
                <% }} else { %>
                <div class="no-comment">暂无评论，快来抢沙发吧~</div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/aplayer/1.10.1/APlayer.min.js"></script>

<script>
    // ==== 初始化 APlayer ====
    const ap = new APlayer({
        container: document.getElementById('aplayer'),
        theme: '#ff0055',
        autoplay: true,
        loop: 'all',
        volume: 0.7,
        audio: [{
            name: '<%= m.getTitle() %>',
            artist: '<%= m.getArtist() %>',
            url: '<%= request.getContextPath() %>/<%= m.getFilePath() %>',
            // 默认封面图
            cover: 'https://p1.music.126.net/K1p6H9l-b8r4xX8f_x8u4A==/109951165792942202.jpg?param=300x300'
        }]
    });

    // ==== WebSocket 弹幕逻辑 ====
    var musicId = "<%= m.getId() %>";
    var wsUrl = "ws://" + window.location.host + "<%= request.getContextPath() %>/danmaku/" + musicId;
    var ws = null;

    try {
        ws = new WebSocket(wsUrl);

        ws.onopen = function() {
            console.log("🔥 弹幕服务器连接成功！");
            showDanmaku("系统提示：欢迎来到直播间！");
        };

        ws.onmessage = function(event) {
            // 收到消息，显示弹幕
            showDanmaku(event.data);
        };

        ws.onclose = function() {
            console.log("弹幕连接断开");
        };
    } catch (e) {
        console.error("WebSocket初始化失败", e);
    }

    // 发送弹幕
    function sendDanmaku() {
        var input = document.getElementById("dmText");
        var text = input.value.trim();
        if(text && ws && ws.readyState === WebSocket.OPEN) {
            ws.send(text); // 1. 发送到服务器
            // 2. 本地不自己画了，等待服务器广播回来，防止双重显示

            input.value = ""; // 清空
        } else if (!ws || ws.readyState !== WebSocket.OPEN) {
            alert("连接已断开，无法发送弹幕");
        }
    }

    // 回车发送监听
    document.getElementById("dmText").addEventListener("keypress", function(e){
        if(e.key === 'Enter') sendDanmaku();
    });

    // 弹幕动画生成函数
    function showDanmaku(text) {
        var container = document.getElementById("danmaku-container");
        var span = document.createElement("span");
        span.className = "danmaku-item";
        span.innerText = text;

        // 随机高度 (10% - 80%)
        var randomTop = Math.floor(Math.random() * 70 + 10);
        span.style.top = randomTop + "%";

        // 随机颜色 (高亮色)
        var colors = ["#ffffff", "#ffe600", "#00ff00", "#00ffff", "#ff00ff", "#ff5e00"];
        span.style.color = colors[Math.floor(Math.random() * colors.length)];

        // 随机字体大小微调
        var size = Math.floor(Math.random() * 10 + 20); // 20px - 30px
        span.style.fontSize = size + "px";

        container.appendChild(span);

        // 动画结束后移除 DOM，防止内存泄漏
        setTimeout(function() {
            span.remove();
        }, 8000); // 必须 >= CSS动画时间
    }
</script>

<% } else { %>
<div style="text-align:center; color:white; margin-top:100px;">
    <h2>🚫 未找到该音乐信息</h2>
    <a href="index" style="color:#007bff;">返回首页</a>
</div>
<% } %>
</body>
</html>