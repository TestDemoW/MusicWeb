package com.music.controller;

import com.music.bean.Music;
import com.music.service.MusicService;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/upload")
@MultipartConfig // ⚠️ 必须加这个注解，才能处理文件上传
public class UploadServlet extends HttpServlet {
    private MusicService service = new MusicService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // 1. 获取普通表单项
        String title = req.getParameter("title");
        String artist = req.getParameter("artist");

        // 2. 获取上传的文件
        Part filePart = req.getPart("file");

        // 3. 生成唯一文件名 (UUID)，防止文件名重复互相覆盖
        String fileName = UUID.randomUUID().toString() + ".mp3";

        // 4. 确定上传路径 (在 webapp/uploads 下)
        String uploadPath = getServletContext().getRealPath("/uploads");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 文件夹不存在则创建
        }

        // 5. 将文件写入磁盘
        filePart.write(uploadPath + File.separator + fileName);

        // 6. 将信息存入数据库
        Music m = new Music();
        m.setTitle(title);
        m.setArtist(artist);
        m.setFilePath("uploads/" + fileName); // 存相对路径
        service.upload(m);

        // 7. 上传完成后重定向回首页
        resp.sendRedirect("index");
    }
}