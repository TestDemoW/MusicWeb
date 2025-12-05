<%--
  Created by IntelliJ IDEA.
  User: w
  Date: 2025/12/6
  Time: 01:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.music.bean.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>编辑用户 - 管理后台</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f4f6f9; display: flex; justify-content: center; padding-top: 50px; }
        .box { width: 450px; background: white; padding: 40px; border-radius: 12px; box-shadow: 0 10px 20px rgba(0,0,0,0.05); }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 5px; color: #666; font-weight: bold; }
        input, select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px; box-sizing: border-box; }
        button { width: 100%; padding: 12px; background: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer; font-size:16px; margin-top: 10px; }
        button:hover { background: #218838; }
        .back-link { display: block; text-align: center; margin-top: 20px; color: #666; text-decoration: none; }
    </style>
</head>
<body>
<%
    // 获取由 Servlet 传递过来的用户信息
    User u = (User)request.getAttribute("editUser");
    if(u == null) { response.sendRedirect("admin"); return; }
%>
<div class="box">
    <h2 style="text-align:center; margin-top:0;">👤 修改用户信息</h2>
    <div style="text-align:center; color:#888; margin-bottom:20px;">正在编辑: <%= u.getUsername() %></div>

    <form action="admin" method="post">
        <input type="hidden" name="action" value="updateUser">
        <input type="hidden" name="id" value="<%= u.getId() %>">

        <div class="form-group">
            <label>昵称</label>
            <input type="text" name="nickname" value="<%= u.getNickname()!=null?u.getNickname():"" %>" required>
        </div>

        <div class="form-group">
            <label>密码 (如需重置，请直接修改)</label>
            <input type="text" name="password" value="<%= u.getPassword() %>" required>
        </div>

        <div class="form-group">
            <label>角色权限</label>
            <select name="role">
                <option value="user" <%= "user".equals(u.getRole())?"selected":"" %>>普通用户 (User)</option>
                <option value="admin" <%= "admin".equals(u.getRole())?"selected":"" %>>管理员 (Admin)</option>
            </select>
        </div>

        <button type="submit">保存修改</button>
    </form>
    <a href="admin?action=list" class="back-link">放弃修改，返回列表</a>
</div>
</body>
</html>