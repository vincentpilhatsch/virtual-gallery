package com.vincent.gallerybackend.user.controller;

import com.vincent.gallerybackend.user.model.Language;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArtworkControllerTests {

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
        UserEntity thomas = new UserEntity("0",
                "thomas",
                "müller",
                "müller@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.DE);
        UserEntity max = new UserEntity("1",
                "max",
                "müller",
                "müller@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.ENG);
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
    void getUserByIdShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserByIdShouldReturnCorrectUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/0")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value("0"))
                        .andExpect(jsonPath("$.firstName").value("thomas"))
                        .andExpect(jsonPath("$.lastName").value("müller"))
                        .andExpect(jsonPath("$.email").value("müller@bht.de"))
                        .andExpect(jsonPath("$.phone").value("123456789"))
                        .andExpect(jsonPath("$.language").value("DE"));
    }

    @Test
    void getUserByIdShouldReturn404WhenUserNotFound() throws Exception {
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
