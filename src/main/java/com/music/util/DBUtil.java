package com.music.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    // 这里的 localhost 是给本地测试用的。部署飞牛时记得改成飞牛IP。
    private static final String URL = "jdbc:mysql://localhost:3306/musicdb?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false";
    private static final String USER = "root";

    // 👇👇👇 修改为你本地 MySQL 的密码 👇👇👇
    private static final String PASS = "password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}