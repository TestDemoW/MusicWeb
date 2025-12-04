<%--
  Created by IntelliJ IDEA.
  User: w
  Date: 2025/12/4
  Time: 23:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>登录</title></head>
<body>
<div style="text-align:center; margin-top:50px;">
    <h2>用户登录</h2>
    <p style="color:red">${msg}</p>
    <form action="auth" method="post">
        <input type="hidden" name="action" value="register">
        <p>账号：<input type="text" name="username" required></p>
        <p>密码：<input type="password" name="password" required></p>
        <button type="submit">登录</button>
    </form>
    <a href="register.jsp">没有账号？去注册</a> | <a href="index">返回首页</a>
</div>
</body>
</html>