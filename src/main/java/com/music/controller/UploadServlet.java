package com.music.controller;

import com.music.bean.Music;
import com.music.bean.User;
import com.music.service.MusicService;
import org.jaudiotagger.audio.AudioFile;     // 引入
import org.jaudiotagger.audio.AudioFileIO;  // 引入

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

        // 1. 检查登录
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        // 2. 获取数据
        String title = req.getParameter("title");
        String artist = req.getParameter("artist");
        Part filePart = req.getPart("file");

        // 3. 保存文件
        String fileName = UUID.randomUUID().toString() + ".mp3";
        String uploadPath = getServletContext().getRealPath("/uploads");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String fullPath = uploadPath + File.separator + fileName;
        filePart.write(fullPath);

        // 4. ✨✨✨ 解析音频时长 (核心修复) ✨✨✨
        String durationStr = "00:00";
        try {
            File mp3File = new File(fullPath);
            AudioFile audioFile = AudioFileIO.read(mp3File);
            int seconds = audioFile.getAudioHeader().getTrackLength();
            // 格式化为 mm:ss
            durationStr = String.format("%02d:%02d", seconds / 60, seconds % 60);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("音频解析失败，使用默认时长");
        }

        // 5. 封装对象
        Music m = new Music();
        m.setTitle(title);
        m.setArtist(artist);
        m.setFilePath("uploads/" + fileName);
        m.setUploaderName(user.getUsername());
        m.setDuration(durationStr); // 设置计算好的时长

        // 6. 保存到数据库
        service.upload(m);

        resp.sendRedirect("index");
    }
}