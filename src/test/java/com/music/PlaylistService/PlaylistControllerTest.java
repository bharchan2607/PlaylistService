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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


}