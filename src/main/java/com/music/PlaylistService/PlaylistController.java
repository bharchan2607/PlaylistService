package com.music.PlaylistService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaylistController {

    PlaylistService service;

    public PlaylistController(PlaylistService service){
        this.service = service;
    }

    @PostMapping("/createPlaylist")
    @ResponseStatus(HttpStatus.CREATED)
    public Playlist createPlaylist(@RequestBody Playlist playlist){
        return service.createPlaylist(playlist);
    }
}
