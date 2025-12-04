package com.music.controller;
import com.music.bean.User;
import com.music.service.MusicService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private MusicService service = new MusicService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // 权限校验：非管理员滚粗
        if (user == null || !"admin".equals(user.getRole())) {
            resp.sendRedirect("index");
            return;
        }

        String action = req.getParameter("action");
        if ("approve".equals(action)) {
            service.approve(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect("admin?action=list");
        } else if ("delete".equals(action)) {
            service.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect("admin?action=list");
        } else {
            // 默认展示管理列表：包括待审核的和所有的
            req.setAttribute("pendingList", service.getPendingList());
            req.setAttribute("allList", service.getAllList());
            req.getRequestDispatcher("admin.jsp").forward(req, resp);
        }
    }
}