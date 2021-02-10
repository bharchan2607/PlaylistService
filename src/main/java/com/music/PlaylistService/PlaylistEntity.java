package com.music.PlaylistService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class PlaylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SongEntity> songs = new ArrayList<>();

    public PlaylistEntity() {
    }

    public PlaylistEntity(String name, SongEntity song) {
        this.name = name;
        this.songs.add(song);
    }

    public PlaylistEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<SongEntity> getSongs() {
        return songs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistEntity that = (PlaylistEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(songs, that.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, songs);
    }
}
