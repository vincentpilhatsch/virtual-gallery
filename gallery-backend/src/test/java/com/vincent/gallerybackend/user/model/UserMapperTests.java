package com.vincent.gallerybackend.user.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class UserMapperTests {

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
        incompleteUserEntity = new UserEntity(UUID.randomUUID(),
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
