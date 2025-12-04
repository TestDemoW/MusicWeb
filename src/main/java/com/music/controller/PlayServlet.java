package com.music.controller;

import com.music.bean.Music;
import com.music.dao.CommentDao; // 👈 关键：必须导入这个包
import com.music.service.MusicService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/play")
public class PlayServlet extends HttpServlet {
    private MusicService service = new MusicService();
    // 实例化 CommentDao，用来查评论
    private CommentDao commentDao = new CommentDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if(idStr != null) {
            int musicId = Integer.parseInt(idStr);

            // 1. 获取音乐详情（同时增加播放次数）
            Music music = service.play(musicId);

            // 2. 获取这首歌的评论列表
            req.setAttribute("commentList", commentDao.getCommentsByMusicId(musicId));

            // 3. 存入请求域并转发
            req.setAttribute("m", music);
            req.getRequestDispatcher("/player.jsp").forward(req, resp);
        }
    }
}