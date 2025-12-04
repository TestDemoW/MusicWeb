package com.music.controller;
import com.music.bean.Comment;
import com.music.bean.User;
import com.music.dao.CommentDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    private CommentDao commentDao = new CommentDao();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");

        if (user != null) {
            Comment c = new Comment();
            c.setMusicId(Integer.parseInt(req.getParameter("musicId")));
            c.setUsername(user.getUsername());
            c.setContent(req.getParameter("content"));
            commentDao.addComment(c);
        }
        // 评论完回到播放页
        resp.sendRedirect("play?id=" + req.getParameter("musicId"));
    }
}