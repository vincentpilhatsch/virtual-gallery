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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        user = new User("0",
                "thomas",
                "müller",
                "müller@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.ENG);
        userEntity = new UserEntity("1",
                "max",
                "müller",
                "müller@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.DE);
        oldUserEntity = new UserEntity("0",
                "thomas",
                "müller",
                "müller@bht.de",
                "müller123",
                "berlin",
                "123456789",
                Language.ENG);
    }

    // getUserById

    @Test
    void getUserById() {
        when(userRepository.findById(userEntity.getId()))
                .thenReturn(Optional.of(userEntity));
        when(userMapper.userDAOtoDTO(userEntity)).thenReturn(user);

        User userDTO = userServiceImpl.getUserById(userEntity.getId());

        verify(userRepository).findById(userEntity.getId());
        verify(userMapper).userDAOtoDTO(userEntity);
        assertEquals(user, userDTO);
    }

    @Test
    void getUserByIdShouldThrowNotFound() {
        when(userRepository.findById("1111"))
                .thenThrow(new NotFoundException("User not found"));

        assertThrows(NotFoundException.class, () ->
                userServiceImpl.getUserById("1111"));
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

    @Test
    void updateUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(userEntity));
        when(userMapper.updateUserDTOtoDAO(oldUserEntity, user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity updatedUser = userServiceImpl.updateUser(user);

        verify(userMapper).updateUserDTOtoDAO(oldUserEntity, user);
        verify(userRepository).save(userEntity);
        assertEquals(userEntity, updatedUser);
    }


}
