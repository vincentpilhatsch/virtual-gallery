package com.vincent.gallerybackend.artwork.model;

import com.vincent.gallerybackend.user.model.Language;
import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.model.UserMapperImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

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

        thomas = new UserEntity(UUID.randomUUID(),
                "thomas",
                "müller",
                "müller@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.DE);
        incompleteUserEntity = new UserEntity(
                UUID.randomUUID(),
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
