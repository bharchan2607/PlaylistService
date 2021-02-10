package com.music.PlaylistService;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    public PlaylistEntity findByName(String name);
}
