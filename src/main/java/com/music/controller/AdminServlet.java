package com.music.controller;

import com.music.bean.User;
import com.music.service.MusicService;
import com.music.dao.InviteDao;
import com.music.dao.UserDao; // 引入 UserDao

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private MusicService service = new MusicService();
    private InviteDao inviteDao = new InviteDao();
    private UserDao userDao = new UserDao(); // ✨ 实例化 UserDao

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // 权限校验
        if (user == null || !"admin".equals(user.getRole())) {
            resp.sendRedirect("index");
            return;
        }

        String action = req.getParameter("action");

        // --- 1. 音乐管理 ---
        if ("approve".equals(action)) {
            service.approve(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect("admin?action=list");
        } else if ("delete".equals(action)) {
            service.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect("admin?action=list");

            // --- 2. 邀请码管理 ---
        } else if ("addCode".equals(action)) {
            String code = req.getParameter("code");
            if(code != null && !code.trim().isEmpty()) inviteDao.addCode(code);
            resp.sendRedirect("admin?action=list");
        } else if ("deleteCode".equals(action)) {
            inviteDao.deleteCode(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect("admin?action=list");

            // --- 3. ✨✨✨ 用户管理 (新增) ✨✨✨ ---
        } else if ("deleteUser".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            // 防止删除自己
            if (id != user.getId()) {
                userDao.deleteUser(id);
            }
            resp.sendRedirect("admin?action=list");

        } else if ("editUser".equals(action)) {
            // 跳转到编辑页面
            int id = Integer.parseInt(req.getParameter("id"));
            User editUser = userDao.getUserById(id);
            req.setAttribute("editUser", editUser);
            req.getRequestDispatcher("admin_user_edit.jsp").forward(req, resp);

        } else {
            // --- 4. 默认展示页面：加载所有数据 ---
            req.setAttribute("pendingList", service.getPendingList());
            req.setAttribute("allList", service.getAllList());
            req.setAttribute("codeList", inviteDao.getAllCodes());

            // ✨ 加载用户列表
            req.setAttribute("userList", userDao.getAllUsers());

            req.getRequestDispatcher("admin.jsp").forward(req, resp);
        }
    }

    // ✨✨✨ [新增] POST 方法处理表单提交 (修改用户信息) ✨✨✨
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // 权限检查
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            resp.sendRedirect("index");
            return;
        }

        String action = req.getParameter("action");

        if ("updateUser".equals(action)) {
            User u = new User();
            u.setId(Integer.parseInt(req.getParameter("id")));
            u.setNickname(req.getParameter("nickname"));
            u.setPassword(req.getParameter("password")); // 管理员重置密码
            u.setRole(req.getParameter("role")); // 修改权限

            userDao.updateUserByAdmin(u);
            resp.sendRedirect("admin?action=list");
        }
    }
}