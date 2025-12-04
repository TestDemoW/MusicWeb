package com.music.controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;

// 1. 定义访问路径，{musicId} 代表房间号，不同歌曲的人互不打扰
@ServerEndpoint("/danmaku/{musicId}")
public class DanmakuServer {

    // 2. 静态变量，用来记录当前所有在线连接
    // Key是musicId(房间号), Value是这个房间里所有的连接Session集合
    private static Map<String, Set<Session>> rooms = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("musicId") String musicId) {
        // 用户连接时触发
        // 如果房间不存在，就创建一个新房间
        rooms.computeIfAbsent(musicId, k -> new CopyOnWriteArraySet<>()).add(session);
        System.out.println("有人进入了歌曲房间: " + musicId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("musicId") String musicId) {
        // 用户关闭网页时触发
        Set<Session> room = rooms.get(musicId);
        if (room != null) {
            room.remove(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("musicId") String musicId) {
        // 3. 收到某人发的弹幕，转发给该房间里的所有人
        System.out.println("收到弹幕: " + message);

        Set<Session> room = rooms.get(musicId);
        if (room != null) {
            for (Session s : room) {
                try {
                    // 这里的 message 可以是简单的文本，也可以是 JSON
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}