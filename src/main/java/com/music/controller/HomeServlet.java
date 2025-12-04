package com.music.controller;

import com.music.service.MusicService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/index")
public class HomeServlet extends HttpServlet {
    private MusicService service = new MusicService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码，防止乱码
        req.setCharacterEncoding("UTF-8");
        // 调用 Service 获取数据，并存入 request 域中，取名叫 "list"
        req.setAttribute("list", service.getHotList());
        // 转发到 index.jsp 页面显示
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}