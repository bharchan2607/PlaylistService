package com.music.PlaylistService;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public SongEntity(String name) {
        this.name = name;
    }

    public SongEntity() {
    }

    public String getName() {
        return name;
    }
}
