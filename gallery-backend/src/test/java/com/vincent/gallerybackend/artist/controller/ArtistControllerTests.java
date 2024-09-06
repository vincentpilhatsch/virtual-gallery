package com.vincent.gallerybackend.artist.controller;

import com.vincent.gallerybackend.artist.model.ArtistEntity;
import com.vincent.gallerybackend.artist.model.Language;
import com.vincent.gallerybackend.artist.repository.ArtistRepository;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArtistControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArtistRepository artistRepository;

    @BeforeEach
    public void setup() {
        initializeDatabase();
        buildMockMvc();
    }

    private void initializeDatabase() {
        ArtistEntity artistEntity = new ArtistEntity(UUID.randomUUID(),
                "müller@bht.de",
                "müller123",
                Language.DE);
        artistRepository.save(artistEntity);
    }

    private void buildMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldInitializeDatabase() {
        List<ArtistEntity> artistEntities = artistRepository.findAll();
        assertFalse(artistEntities.isEmpty());
    }

    // checkArtistExists

    @Test
    void checkArtistExistsShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artist/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void checkArtistExistsShouldReturnFalse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artist/1111")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

}
