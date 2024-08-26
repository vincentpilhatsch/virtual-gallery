package com.vincent.gallerybackend.user.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@SpringBootTest
class ArtworkMapperTests {

    private UserMapperImpl userMapperImpl;
    private UserEntity thomas;
    private UserEntity incompleteUserEntity;

    @BeforeEach
    void setup() {
        userMapperImpl = new UserMapperImpl();

        thomas = new UserEntity("0",
                "thomas",
                "müller",
                "müller@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.DE);
        incompleteUserEntity = new UserEntity(
                "1",
                null,
                "müller",
                null,
                null,
                "berlin",
                "123456789",
                Language.DE
        );
    }

    @Test
    void userDATOtoDTOTest() {
        User user = userMapperImpl.userDAOtoDTO(thomas);

        assertInstanceOf(User.class, user);
        assertEquals("0", user.getId());
        assertEquals("thomas", user.getFirstName());
        assertEquals("müller", user.getLastName());
        assertEquals("müller@bht.de", user.getEmail());
        assertEquals("123456789", user.getPhone());
        assertEquals(Language.DE, user.getLanguage());
    }

    @Test
    void userDAOtoDTOShouldHandleIncompleteData() {
        User user = userMapperImpl.userDAOtoDTO(incompleteUserEntity);

        assertNotNull(user);
        assertNull(user.getFirstName());
        assertNull(user.getEmail());
        assertEquals("berlin", user.getCity());
        assertEquals("123456789", user.getPhone());
        assertEquals(Language.DE, user.getLanguage());
    }

}
