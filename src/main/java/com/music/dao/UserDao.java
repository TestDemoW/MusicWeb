package com.music.dao;

import com.music.bean.User;
import com.music.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                user = mapResultToUser(rs);
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

    // 根据用户名获取信息
    public User getUserByUsername(String username) {
        User user = null;
        try (Connection conn = DBUtil.getConn()) {
            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = mapResultToUser(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return user;
    }

    // 用户更新自己的资料
    public void updateUser(User u) {
        try (Connection conn = DBUtil.getConn()) {
            String sql = "UPDATE users SET nickname=?, bio=?, social_link=?, avatar=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getNickname());
            ps.setString(2, u.getBio());
            ps.setString(3, u.getSocialLink());
            ps.setString(4, u.getAvatar());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ✨✨✨ [新增] 获取所有用户列表 (供管理员使用) ✨✨✨
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConn()) {
            String sql = "SELECT * FROM users ORDER BY id ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultToUser(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ✨✨✨ [新增] 根据ID获取用户 (用于编辑回显) ✨✨✨
    public User getUserById(int id) {
        User user = null;
        try (Connection conn = DBUtil.getConn()) {
            String sql = "SELECT * FROM users WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = mapResultToUser(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return user;
    }

    // ✨✨✨ [新增] 删除用户 ✨✨✨
    public void deleteUser(int id) {
        try (Connection conn = DBUtil.getConn()) {
            // 级联删除通常由数据库外键处理 (ON DELETE CASCADE)
            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ✨✨✨ [新增] 管理员强制更新用户信息 (可改密码、角色) ✨✨✨
    public void updateUserByAdmin(User u) {
        try (Connection conn = DBUtil.getConn()) {
            String sql = "UPDATE users SET nickname=?, role=?, password=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getNickname());
            ps.setString(2, u.getRole());
            ps.setString(3, u.getPassword()); // 管理员可以直接重置密码
            ps.setInt(4, u.getId());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 辅助方法：映射 ResultSet 到 User 对象
    private User mapResultToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // 管理员可能需要看密码
        user.setRole(rs.getString("role"));
        user.setNickname(rs.getString("nickname"));
        user.setBio(rs.getString("bio"));
        user.setSocialLink(rs.getString("social_link"));
        user.setAvatar(rs.getString("avatar"));
        if(user.getAvatar() == null || user.getAvatar().isEmpty()){
            user.setAvatar("https://api.dicebear.com/7.x/identicon/svg?seed=" + user.getUsername());
        }
        return user;
    }
}