package com.music.PlaylistService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addSong/{playlistName}")
    public Playlist addSongsToPlaylist(@PathVariable String playlistName, @RequestBody Song song){
        return service.addSongToPlaylist(playlistName, song);
    }

    @DeleteMapping("/removeSong/{playlistName}/{songName}")
    public Playlist addSongsFromPlaylist(@PathVariable String playlistName, @PathVariable String songName){
        return service.removeSongFromPlaylist(playlistName, songName);
    }
}
