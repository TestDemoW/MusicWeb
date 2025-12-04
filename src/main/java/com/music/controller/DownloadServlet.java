package com.music.controller;

import com.music.bean.Music;
import com.music.service.MusicService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    private MusicService service = new MusicService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取要下载的歌曲ID
        String idStr = req.getParameter("id");
        if(idStr == null) return;

        // 2. 查询歌曲信息 (为了获取真实文件名和存储路径)
        Music m = service.play(Integer.parseInt(idStr)); // 这里复用play方法或者单独写一个getById都行
        if(m == null) {
            resp.getWriter().write("Music not found");
            return;
        }

        // 3. 定位服务器上的文件
        // m.getFilePath() 存的是 "uploads/xxx-uuid.mp3"
        String realPath = getServletContext().getRealPath("/" + m.getFilePath());
        File file = new File(realPath);

        if(!file.exists()) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<h3>抱歉，文件原件已丢失，无法下载。</h3>");
            return;
        }

        // 4. 【关键】设置响应头，强制浏览器下载
        // 拼凑一个好听的文件名： "歌手 - 歌名.mp3"
        String friendlyName = m.getArtist() + " - " + m.getTitle() + ".mp3";
        // 处理中文文件名乱码问题 (URL编码)
        String encodedName = URLEncoder.encode(friendlyName, "UTF-8").replaceAll("\\+", "%20");

        resp.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodedName);
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) file.length());

        // 5. 开始传输文件流
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }
    }
}