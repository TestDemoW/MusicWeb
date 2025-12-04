package com.music.controller;

import com.music.bean.Music;
import com.music.bean.User; // 引入 User 类
import com.music.service.MusicService;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private MusicService service = new MusicService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // 1. 【新增】检查用户是否登录
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // 如果 Session 里没有 user 对象，说明没登录
        if (user == null) {
            // 跳转去登录页
            resp.sendRedirect("login.jsp");
            return; // 结束方法，不再执行后面的上传逻辑
        }

        // 2. 获取表单数据
        String title = req.getParameter("title");
        String artist = req.getParameter("artist");
        Part filePart = req.getPart("file");

        // 3. 处理文件保存
        String fileName = UUID.randomUUID().toString() + ".mp3";
        String uploadPath = getServletContext().getRealPath("/uploads");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        filePart.write(uploadPath + File.separator + fileName);

        // 4. 封装 Music 对象
        Music m = new Music();
        m.setTitle(title);
        m.setArtist(artist);
        m.setFilePath("uploads/" + fileName);

        // 5. 【新增】设置上传者的名字
        // 这样数据库里就能知道这首歌是谁传的了
        m.setUploaderName(user.getUsername());

        // 6. 调用 Service 保存 (Service 会调用 DAO，默认把 status 设为 0 待审核)
        service.upload(m);

        // 7. 上传成功，跳回首页
        resp.sendRedirect("index");
    }
}