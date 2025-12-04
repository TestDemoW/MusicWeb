package com.music.bean;

public class Music {
    private int id;
    private String title;
    private String artist;
    private String filePath;
    private int playCount;
    private int status; // 0待审, 1通过
    private String uploaderName;

    public Music() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getUploaderName() { return uploaderName; }
    public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }
}