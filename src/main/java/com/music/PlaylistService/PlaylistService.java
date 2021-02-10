package com.music.PlaylistService;

import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    PlaylistRepository repository;

    public PlaylistService(PlaylistRepository repository){
        this.repository = repository;
    }

    public Playlist createPlaylist(Playlist playlist) {
        if(playlist.getName() == null || (playlist.getName() != null && playlist.getName().isBlank()) ){
            throw new PlaylistNameRequiredException("Name is Required!!");
        }
        PlaylistEntity entity1 = repository.findByName(playlist.getName());
        if(entity1 != null){
            throw new NameAlreadyExistsException("Name Already Exists!!");
        }
        return mapToPlaylist(repository.save(new PlaylistEntity(playlist.getName())));
    }

    private Playlist mapToPlaylist(PlaylistEntity entity) {
        return new Playlist(entity.getName(), "Successful");
    }
}
