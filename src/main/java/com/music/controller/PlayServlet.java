package com.music.controller;

import com.music.bean.Music;
import com.music.service.MusicService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/play")
public class PlayServlet extends HttpServlet {
    private MusicService service = new MusicService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if(idStr != null) {
            // 业务逻辑：播放一次，计数+1，并查询详情
            Music music = service.play(Integer.parseInt(idStr));

            // 把歌曲信息放入 request，传给 player.jsp
            req.setAttribute("m", music);
            req.getRequestDispatcher("/player.jsp").forward(req, resp);
        }
    }
}