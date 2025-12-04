package com.music.dao;
import com.music.bean.Comment;
import com.music.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    // 添加评论
    public void addComment(Comment c) {
        try (Connection conn = DBUtil.getConn()) {
            String sql = "INSERT INTO comments(music_id, username, content) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, c.getMusicId());
            ps.setString(2, c.getUsername());
            ps.setString(3, c.getContent());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 获取某首歌的所有评论
    public List<Comment> getCommentsByMusicId(int musicId) {
        List<Comment> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConn()) {
            String sql = "SELECT * FROM comments WHERE music_id = ? ORDER BY create_time DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, musicId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Comment c = new Comment();
                c.setUsername(rs.getString("username"));
                c.setContent(rs.getString("content"));
                c.setCreateTime(rs.getString("create_time").substring(0, 19)); // 截取时间
                list.add(c);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}