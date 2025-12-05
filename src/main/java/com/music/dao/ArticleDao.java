package com.music.dao;

import com.music.bean.Article;
import com.music.util.DBUtil;
import java.sql.*;

public class ArticleDao {
    // 查找文章
    public Article getArticleByMusicId(int musicId) {
        Article article = null;
        try (Connection conn = DBUtil.getConn()) {
            String sql = "SELECT * FROM articles WHERE music_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, musicId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                article = new Article();
                article.setId(rs.getInt("id"));
                article.setMusicId(rs.getInt("music_id"));
                article.setContent(rs.getString("content"));
                article.setUpdateTime(rs.getString("update_time"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return article;
    }

    // 保存或更新文章 (如果有就更新，没有就插入)
    public void saveOrUpdate(int musicId, String content) {
        try (Connection conn = DBUtil.getConn()) {
            // 先查一下有没有
            Article exist = getArticleByMusicId(musicId);
            String sql;
            if (exist == null) {
                sql = "INSERT INTO articles(music_id, content) VALUES(?, ?)";
            } else {
                sql = "UPDATE articles SET content = ? WHERE music_id = ?";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            if (exist == null) {
                ps.setInt(1, musicId);
                ps.setString(2, content);
            } else {
                ps.setString(1, content);
                ps.setInt(2, musicId);
            }
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}