package com.music.PlaylistService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Entity;

import java.util.List;

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

    @Test
    public void addSongToPlaylist(){
        Song song = new Song("Kuch Kuch Hota Hai");
        PlaylistEntity entity = new PlaylistEntity("Classic");
        PlaylistEntity entity1 = new PlaylistEntity("Classic", new SongEntity("Kuch Kuch Hota Hai"));
        Playlist playlist = new Playlist("Classic", song);
        when(repository.findByName("Classic")).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity1);
        Playlist actualPlaylist = service.addSongToPlaylist("Classic", new Song("Kuch Kuch Hota Hai"));
        verify(repository, times(1)).findByName("Classic");
        verify(repository,times(1)).save(entity);
        assertEquals(playlist.getSongs().size(), actualPlaylist.getSongs().size());
        assertEquals(playlist.getSongs().get(0).getName(), actualPlaylist.getSongs().get(0).getName());
    }

    @Test
    public void addSongToPlaylist_PlaylistNotFound(){
        when(repository.findByName("Classic")).thenReturn(null);
        PlaylistNotFoundException exception = assertThrows(PlaylistNotFoundException.class,
                ()-> service.addSongToPlaylist("Classic", new Song("Kuch Kuch Hota Hai")));
        assertEquals("Playlist Not Found!!",exception.getMessage());
        verify(repository, times(1)).findByName("Classic");
    }

    @Test
    public void removeSongFromPlaylist(){
        Song song = new Song("Kuch Kuch Hota Hai");
        PlaylistEntity entity = new PlaylistEntity("Classic");
        PlaylistEntity entity1 = new PlaylistEntity("Classic", new SongEntity("Kuch Kuch Hota Hai"));
        Playlist playlist = new Playlist("Classic", song);
        when(repository.findByName("Classic")).thenReturn(entity1);
        when(repository.save(entity1)).thenReturn(entity);

        Playlist actualPlaylist = service.removeSongFromPlaylist("Classic", "Kuch Kuch Hota Hai");
        verify(repository, times(1)).findByName("Classic");
        verify(repository,times(1)).save(entity);
        assertEquals(0, actualPlaylist.getSongs().size());
    }

    @Test
    public void removeSongFromPlaylist_SongNotFound(){
        PlaylistEntity entity1 = new PlaylistEntity("Classic", new SongEntity("Kuch Kuch Hota Hai"));
        when(repository.findByName("Classic")).thenReturn(entity1);
        SongNotFoundException exception = assertThrows(SongNotFoundException.class,
                ()-> service.removeSongFromPlaylist("Classic", "Main Hoon Na"));
        assertEquals("Song Not Found in the Playlist",exception.getMessage());
        verify(repository, times(1)).findByName("Classic");

    }

    @Test
    public void removeSongFromPlaylist_PlaylistNotFound(){
        PlaylistEntity entity1 = new PlaylistEntity("Classic", new SongEntity("Kuch Kuch Hota Hai"));
        when(repository.findByName("Classic")).thenReturn(null);
        PlaylistNotFoundException exception = assertThrows(PlaylistNotFoundException.class,
                ()-> service.removeSongFromPlaylist("Classic", "Main Hoon Na"));
        assertEquals("Playlist Not Found!!",exception.getMessage());
        verify(repository, times(1)).findByName("Classic");

    }
}
