package com.td.hung.musicdemo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hung Tran on 06/11/2017.
 */

public class PlayList implements Serializable {
    private String name;
    private List<Song> listSong;

    public PlayList(String name, List<Song> listSong) {
        this.name = name;
        this.listSong = listSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public void setListSong(List<Song> listSong) {
        this.listSong = listSong;
    }
}
