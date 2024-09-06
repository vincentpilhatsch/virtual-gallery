package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.user.model.*;
import com.vincent.gallerybackend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    private User user;

    private UserEntity oldUserEntity;

    private UserEntity userEntity;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserMapper userMapper;


    @BeforeEach
    void setup() {
        user = new User(UUID.randomUUID(),
                "thomas",
                "müller",
                "müller@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.ENG);
        userEntity = new UserEntity(UUID.randomUUID(),
                "max",
                "meier",
                "meier@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.DE);
        oldUserEntity = new UserEntity(UUID.randomUUID(),
                "thomas",
                "bäcker",
                "bäcker@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.ENG);
    }

    // getUserById

    @Test
    void findByEmail() {
        when(userRepository.findByEmail(userEntity.getEmail()))
                .thenReturn(Optional.of(userEntity));
        when(userMapper.userDAOtoDTO(userEntity)).thenReturn(user);

        User userDTO = userServiceImpl.getUserByEmail(userEntity.getEmail());

        verify(userRepository).findByEmail(userEntity.getEmail());
        verify(userMapper).userDAOtoDTO(userEntity);
        assertEquals(user, userDTO);
    }

    @Test
    void getUserByEmailShouldThrowNotFound() {
        when(userRepository.findByEmail("1111"))
                .thenThrow(new NotFoundException("User not found"));

        assertThrows(NotFoundException.class, () ->
                userServiceImpl.getUserByEmail("1111"));
    }

    // createUser

    @Test
    void createUser() {
        when(userMapper.userDTOtoDAO(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity userDAO = userServiceImpl.createUser(user);

        verify(userMapper).userDTOtoDAO(user);
        verify(userRepository).save(userEntity);
        assertEquals(userEntity, userDAO);
    }

    @Test
    void createUserShouldHandleMapperException() {
        when(userMapper.userDTOtoDAO(user)).thenThrow(new RuntimeException("Mapper error"));

        assertThrows(RuntimeException.class, () -> {
            userServiceImpl.createUser(user);
        });
    }

    @Test
    void createUserShouldHandleRepositoryException() {
        when(userRepository.save(userEntity)).thenThrow(new RuntimeException("Repository error"));

        assertThrows(RuntimeException.class, () -> {
            userServiceImpl.createUser(user);
        });
    }

    // updateUser

//    @Test
//    void updateUser() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(userEntity));
//        when(userMapper.updateUserDTOtoDAO(oldUserEntity, user)).thenReturn(userEntity);
//        when(userRepository.save(userEntity)).thenReturn(userEntity);
//
//        UserEntity updatedUser = userServiceImpl.updateUser(user);
//
//        verify(userMapper).updateUserDTOtoDAO(oldUserEntity, user);
//        verify(userRepository).save(userEntity);
//        assertEquals(userEntity, updatedUser);
//    }

}
