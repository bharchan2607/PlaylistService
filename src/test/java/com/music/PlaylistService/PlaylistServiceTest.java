package com.music.PlaylistService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

    @Mock
    PlaylistRepository repository;
    @InjectMocks
    PlaylistService service;

    @Test
    public void createPlaylist(){
        PlaylistEntity entity = new PlaylistEntity("classic");
        Playlist playlist = new Playlist("classic","Successful");
        when(repository.save(entity)).thenReturn(entity);
        Playlist actualPlaylist = service.createPlaylist(playlist);
        verify(repository,times(1)).save(entity);
        assertEquals(playlist.getName(),actualPlaylist.getName());
        assertEquals(playlist.getMessage(),actualPlaylist.getMessage());

    }

    @Test
    public void createPlaylist_ExistingName(){
        PlaylistEntity entity = new PlaylistEntity("classic");
        Playlist playlist = new Playlist("classic","UnSuccessful");
        when(repository.findByName("classic")).thenReturn(entity);
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                ()-> service.createPlaylist(playlist));
        assertEquals("Name Already Exists!!",exception.getMessage());
        verify(repository,times(1)).findByName("classic");


    }

    @Test
    public void createPlaylist_NoName(){
        Playlist playlist = new Playlist();
        PlaylistNameRequiredException exception = assertThrows(PlaylistNameRequiredException.class, ()-> service.createPlaylist(playlist));
        assertEquals("Name is Required!!",exception.getMessage());

    }

    @Test
    public void createPlaylist_BlankName(){
        Playlist playlist = new Playlist("");
        PlaylistNameRequiredException exception = assertThrows(PlaylistNameRequiredException.class, ()-> service.createPlaylist(playlist));
        assertEquals("Name is Required!!",exception.getMessage());

    }
}
