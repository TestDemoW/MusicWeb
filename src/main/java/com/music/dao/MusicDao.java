package com.music.dao;

import com.music.bean.Music;
import com.music.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicDao {
    // 获取列表
    public List<Music> getHotMusic() {
        List<Music> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConn()) {
            String sql = "SELECT * FROM music ORDER BY play_count DESC LIMIT 20";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Music m = new Music();
                m.setId(rs.getInt("id"));
                m.setTitle(rs.getString("title"));
                m.setArtist(rs.getString("artist"));
                m.setFilePath(rs.getString("file_path"));
                m.setPlayCount(rs.getInt("play_count"));
                list.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 保存
    public void saveMusic(Music m) {
        try (Connection conn = DBUtil.getConn()) {
            String sql = "INSERT INTO music(title, artist, file_path) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, m.getTitle());
            ps.setString(2, m.getArtist());
            ps.setString(3, m.getFilePath());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 增加播放数
    public void addPlayCount(int id) {
        try (Connection conn = DBUtil.getConn()) {
            String sql = "UPDATE music SET play_count = play_count + 1 WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 查单曲
    public Music getMusicById(int id) {
        Music m = null;
        try (Connection conn = DBUtil.getConn()) {
            String sql = "SELECT * FROM music WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                m = new Music();
                m.setId(rs.getInt("id"));
                m.setTitle(rs.getString("title"));
                m.setArtist(rs.getString("artist"));
                m.setFilePath(rs.getString("file_path"));
                m.setPlayCount(rs.getInt("play_count"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return m;
    }
}