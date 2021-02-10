package com.music.PlaylistService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
