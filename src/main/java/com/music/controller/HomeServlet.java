package com.music.controller;

import com.music.dao.MusicDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/index")
public class HomeServlet extends HttpServlet {
    private MusicDao dao = new MusicDao();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // 1. 获取分页参数
        String pageStr = req.getParameter("page");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try { page = Integer.parseInt(pageStr); } catch (Exception e) {}
        }
        int pageSize = 10;

        // 2. 获取搜索关键词
        String keyword = req.getParameter("keyword");

        // 3. 判断是搜索模式还是榜单模式
        if (keyword != null && !keyword.trim().isEmpty()) {
            // === 搜索模式 ===
            keyword = keyword.trim();
            req.setAttribute("list", dao.searchMusic(keyword, page, pageSize));

            // 计算搜索结果的分页
            int totalCount = dao.getSearchCount(keyword);
            int totalPage = (int) Math.ceil((double) totalCount / pageSize);
            if (totalPage == 0) totalPage = 1;

            req.setAttribute("isSearch", true); // 标记当前是搜索状态
            req.setAttribute("keyword", keyword); // 回填关键词
            req.setAttribute("totalPage", totalPage);

        } else {
            // === 普通榜单模式 ===
            String tab = req.getParameter("tab");
            if (tab == null || tab.isEmpty()) tab = "hot";

            req.setAttribute("list", dao.getMusicList(tab, page, pageSize));

            int totalCount = dao.getMusicCount();
            int totalPage = (int) Math.ceil((double) totalCount / pageSize);
            if (totalPage == 0) totalPage = 1;

            req.setAttribute("currTab", tab);
            req.setAttribute("totalPage", totalPage);
        }

        req.setAttribute("currPage", page);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}