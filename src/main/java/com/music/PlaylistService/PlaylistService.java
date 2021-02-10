package com.music.PlaylistService;

import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    PlaylistRepository repository;

    public PlaylistService(PlaylistRepository repository){
        this.repository = repository;
    }

    public Playlist createPlaylist(Playlist playlist) {
        PlaylistEntity entity = repository.save(new PlaylistEntity(playlist.getName()));
        Playlist play = null;
        if(entity != null) {
            play = mapToPlaylist(entity);
            play.setMessage("Successful");
            return play;
        }
        return new Playlist(playlist.getName(),"UnSuccessful");
    }

    private Playlist mapToPlaylist(PlaylistEntity entity) {
        return new Playlist(entity.getName());
    }
}
