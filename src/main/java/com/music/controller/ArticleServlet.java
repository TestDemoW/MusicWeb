package com.music.controller;

import com.music.bean.Article;
import com.music.bean.Music;
import com.music.bean.User;
import com.music.dao.ArticleDao;
import com.music.service.MusicService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/article")
public class ArticleServlet extends HttpServlet {
    private ArticleDao articleDao = new ArticleDao();
    private MusicService musicService = new MusicService(); // 用来查歌曲信息和上传者

    // GET: 查看文章页面
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String musicIdStr = req.getParameter("musicId");
        if (musicIdStr == null) { resp.sendRedirect("index"); return; }

        int musicId = Integer.parseInt(musicIdStr);

        // 1. 获取文章内容
        Article article = articleDao.getArticleByMusicId(musicId);

        // 2. 获取歌曲信息 (为了显示标题和判断权限)
        Music music = musicService.play(musicId); // 注意：这里复用play会增加播放量，介意的话可以在Service加个只查不加量的方法

        // 3. 权限判断：当前用户是不是作者？
        boolean isAuthor = false;
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser != null && music != null && currentUser.getUsername().equals(music.getUploaderName())) {
            isAuthor = true;
        }

        req.setAttribute("article", article);
        req.setAttribute("music", music);
        req.setAttribute("isAuthor", isAuthor);

        req.getRequestDispatcher("article.jsp").forward(req, resp);
    }

    // POST: 作者保存文章
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // 1. 再次校验权限 (防止通过POST接口强行修改)
        User currentUser = (User) req.getSession().getAttribute("user");
        int musicId = Integer.parseInt(req.getParameter("musicId"));
        Music music = musicService.play(musicId);

        if (currentUser != null && music != null && currentUser.getUsername().equals(music.getUploaderName())) {
            String content = req.getParameter("content");
            articleDao.saveOrUpdate(musicId, content);
        }

        // 保存完回到查看页
        resp.sendRedirect("article?musicId=" + musicId);
    }
}