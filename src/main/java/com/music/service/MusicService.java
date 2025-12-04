package com.music.service;

import com.music.bean.Music;
import com.music.dao.MusicDao;
import java.util.List;

public class MusicService {
    private MusicDao dao = new MusicDao();

    public List<Music> getHotList() {
        return dao.getHotMusic();
    }

    public void upload(Music music) {
        dao.saveMusic(music);
    }

    public Music play(int id) {
        dao.addPlayCount(id);
        return dao.getMusicById(id);
    }
}