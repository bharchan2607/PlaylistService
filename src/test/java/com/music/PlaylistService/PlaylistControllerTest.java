package com.music.PlaylistService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PlaylistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    PlaylistRepository repository;

    @BeforeEach
    public void setup(){
        repository.deleteAll();
    }

    @Test
    public void createPlaylist() throws Exception {
        Playlist playlist = new Playlist("Classic");
        mockMvc.perform(post("/createPlaylist")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(playlist)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successful"))
                .andExpect(jsonPath("$.name").value("Classic"))
                .andExpect(jsonPath("$.songs").isEmpty())
                .andDo(document("createPlaylist",
                        requestFields(fieldWithPath("name").description("Name of the Playlist"),
                                fieldWithPath("message").optional().description("Success if playlist is created"),
                                fieldWithPath("songs").optional().description("Array of Songs")),
                        responseFields(fieldWithPath("message").description("Success if playlist is created"),
                                fieldWithPath("name").description("Name of the playlist"),
                                fieldWithPath("songs").description("Array of Songs"))));

    }

    @Test
    public void createPlaylist_existingName() throws Exception {
        Playlist playlist = new Playlist("Classic");
        mockMvc.perform(post("/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist)))
                .andExpect(status().isConflict())
                .andExpect(result ->  assertTrue(result.getResolvedException()
                        instanceof NameAlreadyExistsException))
                .andExpect(result -> assertEquals("Name Already Exists!!",
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void createPlaylist_NoName() throws Exception {
        Playlist playlist = new Playlist();
        mockMvc.perform(post("/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->  assertTrue(result.getResolvedException()
                        instanceof PlaylistNameRequiredException))
                .andExpect(result -> assertEquals("Name is Required!!",
                        result.getResolvedException().getMessage()));

    }

    @Test
    public void AddSongToPlaylist() throws Exception {
        Song song = new Song("Kuch Kuch Hota Hai");
        Playlist playlist = new Playlist("Classic");

        mockMvc.perform(post("/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist)))
                .andExpect(status().isCreated());

        String playlistName = "Classic";
        mockMvc.perform(post("/addSong/{playlistName}",playlistName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(playlistName))
                .andExpect(jsonPath("$.songs.[0].name").value("Kuch Kuch Hota Hai"))
                .andDo(document("addSong",
                        pathParameters(
                                parameterWithName("playlistName").description("Playlist Name")),
                        responseFields(
                                fieldWithPath("name").description("Playlist Name"),
                                fieldWithPath("message").description("Success if the playlist is created"),
                                fieldWithPath("songs").description("Songs in the Playlist"),
                                fieldWithPath("songs.[].name").description("Name of the Song in the Playlist"))
                        ));

    }

    @Test
    public void addSongsToPlaylist_PlaylistNotFound() throws Exception {
        Playlist playlist = new Playlist();
        String playlistName ="Classic";
        mockMvc.perform(post("/addSong/{playlistName}",playlistName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Song("Main Hoon Na"))))
                .andExpect(status().isNotFound())
                .andExpect(result ->  assertTrue(result.getResolvedException()
                        instanceof PlaylistNotFoundException))
                .andExpect(result -> assertEquals("Playlist Not Found!!",
                        result.getResolvedException().getMessage()));

    }

    @Test
    public void removeSongFromPlaylist() throws Exception {
        Song song = new Song("Kuch Kuch Hota Hai");
        Song song1 = new Song("Main Hoon Na");
        Playlist playlist = new Playlist("Classic");

        mockMvc.perform(post("/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist)))
                .andExpect(status().isCreated());

        String playlistName = "Classic";
        mockMvc.perform(post("/addSong/{playlistName}",playlistName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/addSong/{playlistName}",playlistName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song1)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/removeSong/{playlistName}/{songName}",playlistName,song.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(playlistName))
                .andExpect(jsonPath("$.songs.[0].name").value("Main Hoon Na"))
                .andExpect(jsonPath("$.songs").value(hasSize(1)))
                .andDo(document("removeSong",
                        pathParameters(
                                parameterWithName("playlistName").description("Playlist Name"),
                                parameterWithName("songName").description("Song Name to be removed")),
                        responseFields(
                                fieldWithPath("name").description("Playlist Name"),
                                fieldWithPath("message").description("Success if the playlist is created"),
                                fieldWithPath("songs").description("Songs in the Playlist"),
                                fieldWithPath("songs.[].name").description("Name of the Song in the Playlist"))
                ));
    }

    @Test
    public void removeSongFromPlaylist_SongNotFound() throws Exception {
        Song song = new Song("Kuch Kuch Hota Hai");
        Song song1 = new Song("Main Hoon Na");
        Playlist playlist = new Playlist("Classic");

        mockMvc.perform(post("/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist)))
                .andExpect(status().isCreated());

        String playlistName = "Classic";
        mockMvc.perform(post("/addSong/{playlistName}", playlistName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song)))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/removeSong/{playlistName}/{songName}", playlistName, song1.getName()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof SongNotFoundException))
                .andExpect(result -> assertEquals("Song Not Found in the Playlist",
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void removeSongFromPlaylist_PlaylistNotFound() throws Exception {

        mockMvc.perform(delete("/removeSong/{playlistName}/{songName}","Classic","Main Hoon Na"))
                .andExpect(status().isNotFound())
                .andExpect(result ->  assertTrue(result.getResolvedException()
                        instanceof PlaylistNotFoundException))
                .andExpect(result -> assertEquals("Playlist Not Found!!",
                        result.getResolvedException().getMessage()));

    }

    @Test
    public void getAllSongsFromPlaylist() throws Exception {
        Song song = new Song("Kuch Kuch Hota Hai");
        Song song1 = new Song("Main Hoon Na");
        Playlist playlist = new Playlist("Classic");

        mockMvc.perform(post("/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(playlist)))
                .andExpect(status().isCreated());

        String playlistName = "Classic";
        mockMvc.perform(post("/addSong/{playlistName}",playlistName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/addSong/{playlistName}",playlistName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song1)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/fetchAll/{playlistName}","Classic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Classic"))
                .andExpect(jsonPath("$.songs.[0].name").value("Kuch Kuch Hota Hai"))
                .andExpect(jsonPath("$.songs.[1].name").value("Main Hoon Na"))
                .andExpect(jsonPath("$.songs").value(hasSize(2)))
                .andDo(document("getAllSongs",
                        pathParameters(
                                parameterWithName("playlistName").description("Playlist Name")),
                        responseFields(
                                fieldWithPath("name").description("Playlist Name"),
                                fieldWithPath("message").description("Success if the playlist is created"),
                                fieldWithPath("songs").description("Songs in the Playlist"),
                                fieldWithPath("songs.[].name").description("Name of the Song in the Playlist"))
                ));

    }

    @Test
    public void fetchAllSongsFromPlaylist_PlaylistNotFound() throws Exception {

        mockMvc.perform(get("/fetchAll/{playlistName}","Classic"))
                .andExpect(status().isNotFound())
                .andExpect(result ->  assertTrue(result.getResolvedException()
                        instanceof PlaylistNotFoundException))
                .andExpect(result -> assertEquals("Playlist Not Found!!",
                        result.getResolvedException().getMessage()));

    }

}