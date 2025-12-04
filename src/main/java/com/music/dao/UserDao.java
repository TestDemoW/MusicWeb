package com.music.dao;
import com.music.bean.User;
import com.music.util.DBUtil;
import java.sql.*;

public class UserDao {
    // 登录验证
    public User login(String username, String password) {
        User user = null;
        try (Connection conn = DBUtil.getConn()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return user;
    }

    // 注册
    public boolean register(String username, String password) {
        try (Connection conn = DBUtil.getConn()) {
            String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}