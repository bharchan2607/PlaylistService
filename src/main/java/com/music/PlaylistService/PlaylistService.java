package com.music.PlaylistService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Song> songs = entity.getSongs().stream()
                .map(songEntity -> new Song(songEntity.getName()))
                .collect(Collectors.toList());
        return new Playlist(entity.getName(), "Successful", songs);
    }

    public Playlist addSongToPlaylist(String playlistName, Song song) {
        PlaylistEntity entity = repository.findByName(playlistName);
        if(entity !=null){
            entity.getSongs().add(new SongEntity(song.getName()));
            return mapToPlaylist(repository.save(entity));
        }else{
            throw new PlaylistNotFoundException("Playlist Not Found!!");
        }
    }

    public Playlist removeSongFromPlaylist(String playlistName, String songName) {
        PlaylistEntity entity = repository.findByName(playlistName);
        if(entity == null){
            throw new PlaylistNotFoundException("Playlist Not Found!!");
        }
        SongEntity songTobeRemoved = null;
        boolean removeSong = false;
       for(SongEntity song: entity.getSongs()){
           if(song.getName().equals(songName)){
               songTobeRemoved = song;
               removeSong = true;
           }
       }
       if(removeSong) {
           entity.getSongs().remove(songTobeRemoved);
           return mapToPlaylist(repository.save(entity));
       }
       throw new SongNotFoundException("Song Not Found in the Playlist");
    }

    public Playlist fetchAllSongsFromPlaylist(String playlistName) {
        PlaylistEntity entity = repository.findByName(playlistName);
        if(entity == null){
            throw new PlaylistNotFoundException("Playlist Not Found!!");
        }
        return mapToPlaylist(entity);
    }
}
