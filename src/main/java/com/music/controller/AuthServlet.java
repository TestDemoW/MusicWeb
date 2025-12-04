package com.music.controller;
import com.music.bean.User;
import com.music.dao.UserDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("login".equals(action)) {
            User user = userDao.login(req.getParameter("username"), req.getParameter("password"));
            if (user != null) {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect("index");
            } else {
                req.setAttribute("msg", "账号或密码错误");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } else if ("register".equals(action)) {
            boolean success = userDao.register(req.getParameter("username"), req.getParameter("password"));
            if (success) {
                resp.sendRedirect("login.jsp");
            } else {
                req.setAttribute("msg", "注册失败，用户名可能已存在");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
            }
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 注销功能
        if ("logout".equals(req.getParameter("action"))) {
            req.getSession().invalidate();
            resp.sendRedirect("index");
        }
    }
}