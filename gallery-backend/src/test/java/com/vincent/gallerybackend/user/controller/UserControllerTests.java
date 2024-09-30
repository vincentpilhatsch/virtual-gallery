package com.vincent.gallerybackend.user.controller;

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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class UserControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        initializeDatabase();
        buildMockMvc();
    }

    private void initializeDatabase() {
        UserEntity thomas = new UserEntity(UUID.randomUUID(),
                "thomas",
                "bäcker",
                "bäcker@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.DE);
        UserEntity max = new UserEntity(UUID.randomUUID(),
                "max",
                "schultz",
                "schultz@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.EN);
        userRepository.saveAll(List.of(thomas, max));
    }

    private void buildMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldInitializeDatabase() {
        List<UserEntity> userEntities = userRepository.findAll();
        assertFalse(userEntities.isEmpty());
    }

    // getUserById

    @Test
    void getUserByEmailShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/bäcker@bht.de")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserByIdShouldReturnCorrectUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/bäcker@bht.de")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.firstName").value("thomas"))
                        .andExpect(jsonPath("$.lastName").value("bäcker"))
                        .andExpect(jsonPath("$.email").value("bäcker@bht.de"))
                        .andExpect(jsonPath("$.phone").value("123456789"))
                        .andExpect(jsonPath("$.language").value("DE"));
    }

    @Test
    void getUserByEmailShouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1111")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    // createUser

    @Test
    void createUserShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content("""
                                {
                                  "firstName": "Sebastian",
                                  "lastName": "bäcker",
                                  "email": "reiner@bht.de",
                                  "password": "bäcker",
                                  "city": "Berlin",
                                  "phone": "123456789",
                                  "language": "DE"
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
                                  "firstName": "Sebastian",
                                  "lastName": "bäcker",
                                  "email": "reiner@bht.de",
                                  "password": "bäcker",
                                  "city": "Berlin",
                                  "phone": "123456789",
                                  "language": "DE"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Sebastian"))
                .andExpect(jsonPath("$.lastName").value("bäcker"))
                .andExpect(jsonPath("$.email").value("reiner@bht.de"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.language").value("DE"));;
    }

    @Test
    void createUserWithInvalidContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content("""
                                {
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
                                  "firstName": "MAX",
                                  "lastName": "bäcker",
                                  "email": "bäcker@bht.de",
                                  "password": "bäcker",
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
        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .content("""
                                {
                                  "firstName": "MAX",
                                  "lastName": "bäcker",
                                  "email": "bäcker@bht.de",
                                  "password": "bäcker",
                                  "city": "Berlin",
                                  "phone": "123456789",
                                  "language": "DE"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("MAX"))
                .andExpect(jsonPath("$.lastName").value("bäcker"))
                .andExpect(jsonPath("$.email").value("bäcker@bht.de"))
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
                                  "lastName": "bäcker",
                                  "email": "bäcker@bht.de",
                                  "password": "bäcker",
                                  "city": "Berlin",
                                  "phone": "123456789",
                                  "language": "DE"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
