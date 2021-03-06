package com.td.hung.musicdemo.entity;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PC on 15/10/2017.
 */

public class Song implements Serializable {
    private String path;
    private long id;
    private String title;
    private String artist;
    private int index;
    

    public Song(String path, long songID, String songTitle, String songArtist, int index) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        this.path = path;
        this.index = index;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public int getIndex() {
        return index;
    }


    public String toString() {
        Gson gson = new Gson();
        String jsonInString = gson.toJson(this);
        return jsonInString;
    }
}
