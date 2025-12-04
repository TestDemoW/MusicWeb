package com.music.bean;
public class Comment {
    private int id;
    private int musicId;
    private String username;
    private String content;
    private String createTime; // 为了方便，这里用String接收时间

    // 必须生成 Getter/Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMusicId() { return musicId; }
    public void setMusicId(int musicId) { this.musicId = musicId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}