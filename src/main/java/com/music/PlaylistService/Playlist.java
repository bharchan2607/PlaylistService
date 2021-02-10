package com.music.PlaylistService;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private String message;
    private List<Song> songs = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public Playlist(String name, Song song) {
        this.name = name;
        this.songs.add(song);
    }

    public Playlist(String name, String message, List<Song> songs) {
        this.name = name;
        this.message = message;
        this.songs = songs;
    }

    public Playlist() {
    }

    public Playlist(String name, String message) {
        this.name = name;
        this.message = message;
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
