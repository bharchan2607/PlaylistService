package com.music.PlaylistService;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private String message;
    private List<Song> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public Playlist() {
    }

    public Playlist(String name, String message) {
        this.name = name;
        this.message = message;
        this.songs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
