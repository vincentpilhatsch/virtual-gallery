package com.vincent.gallerybackend.artwork.controller;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.user.model.Language;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@ActiveProfiles("test")
@SpringBootTest
class ArtworkControllerTests {

    private MockMvc mockMvc;

    private LocalDateTime fixedLocalDateTime;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArtworkRepository artworkRepository;

    @BeforeEach
    public void setup() {
        artworkRepository.deleteAll();
        initializeDatabase();
        buildMockMvc();
    }

    private void initializeDatabase() {
        fixedLocalDateTime = LocalDateTime.now();

        ArtworkEntity drippingWater = new ArtworkEntity(
                1L,
                "Dripping Water",
                120,
                160,
                "2021",
                fixedLocalDateTime,
                "Gemalt mit Pinsel und Sprühdose",
                new BigDecimal("123.45"),
                false,
                new byte[]{'1', '2', '3'}
        );
        ArtworkEntity burningFire = new ArtworkEntity(
                2L,
                "Burning Fire",
                120,
                160,
                "2021",
                fixedLocalDateTime,
                "Gemalt mit Pinsel und Sprühdose",
                new BigDecimal("123.45"),
                false,
                new byte[]{'1', '2', '3'}
        );

        artworkRepository.saveAll(List.of(drippingWater, burningFire));

    }

    private void buildMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldInitializeDatabase() {
        List<ArtworkEntity> artworkEntities = artworkRepository.findAll();
        assertFalse(artworkEntities.isEmpty());
    }

    // getUserById

    @Test
    void getUserByIdShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artwork/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserByIdShouldReturnCorrectArtwork() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artwork/1")
                        .contentType(MediaType.APPLICATION_JSON))
                         .andExpect(jsonPath("$.title").value("Dripping Water"))
                        .andExpect(jsonPath("$.width").value("120"))
                        .andExpect(jsonPath("$.height").value("160"))
                        .andExpect(jsonPath("$.yearOfCreation").value("2021"))
                        .andExpect(jsonPath("$.technic").value("Gemalt mit Pinsel und Sprühdose"))
                        .andExpect(jsonPath("$.price").value(new BigDecimal("123.45")))
                        .andExpect(jsonPath("$.sold").value(false))
                        .andExpect(jsonPath("$.image").value("MTIz"));
    }

    @Test
    void getUserByIdShouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artist/1111")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    // createUser

    @Test
    void createUserShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/artist")
                        .content("""
                                {
                                  "id": 10,
                                  "title": "XXX",
                                  "width": 120,
                                  "height": 160,
                                  "yearOfCreation": "2021",
                                  "uploadedAt": "2024-08-26T21:06:23.416067",
                                  "technic": "Gemalt mit Pinsel und Sprühdose",
                                  "price": 123.45,
                                  "sold": false,
                                  "image": "MTIz"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
    }

    @Test
    void createUserShouldReturnUserCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content("""
                                {
                                  "id": "10",
                                  "firstName": "Sebastian",
                                  "lastName": "müller",
                                  "email": "müller@bht.de",
                                  "password": "müller123",
                                  "city": "Berlin",
                                  "phone": "123456789",
                                  "language": "DE"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("10"))
                .andExpect(jsonPath("$.firstName").value("Sebastian"))
                .andExpect(jsonPath("$.lastName").value("müller"))
                .andExpect(jsonPath("$.email").value("müller@bht.de"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.language").value("DE"));;
    }

    @Test
    void createUserWithInvalidContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content("""
                                {
                                  "id": "10",
                                  "firstName": "Sebastian",
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // updateUser

    @Test
    void updateUserShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .content("""
                                {
                                  "id": "1",
                                  "firstName": "MAX",
                                  "lastName": "müller",
                                  "email": "müller@bht.de",
                                  "password": "müller123",
                                  "city": "Berlin",
                                  "phone": "123456789",
                                  "language": "DE"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserShouldReturnUpdatedUserCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content("""
                                {
                                  "id": "1",
                                  "firstName": "MAX",
                                  "lastName": "müller",
                                  "email": "müller@bht.de",
                                  "password": "müller123",
                                  "city": "Berlin",
                                  "phone": "123456789",
                                  "language": "DE"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstName").value("MAX"))
                .andExpect(jsonPath("$.lastName").value("müller"))
                .andExpect(jsonPath("$.email").value("müller@bht.de"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.language").value("DE"));;
    }

    @Test
    void updateUserWithInvalidContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .content("""
                                {
                                  "id": "1111",
                                  "firstName": "MAX",
                                  "lastName": "müller",
                                  "email": "müller@bht.de",
                                  "password": "müller123",
                                  "city": "Berlin",
                                  "phone": "123456789",
                                  "language": "DE"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
