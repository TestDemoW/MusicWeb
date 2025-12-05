package com.music.bean;

public class Article {
    private int id;
    private int musicId;
    private String content;
    private String updateTime;

    // Getter & Setter (一定要生成!)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMusicId() { return musicId; }
    public void setMusicId(int musicId) { this.musicId = musicId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}