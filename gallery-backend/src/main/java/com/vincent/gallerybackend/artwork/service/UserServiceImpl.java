package com.vincent.gallerybackend.artwork.service;

import com.vincent.gallerybackend.artwork.model.User;
import com.vincent.gallerybackend.artwork.model.UserEntity;
import com.vincent.gallerybackend.artwork.model.UserMapper;
import com.vincent.gallerybackend.artwork.repository.UserRepository;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public User getUserById(String id) {
        UserEntity userEntity = findById(id);
        return userMapper.userDAOtoDTO(userEntity);
    }


    private UserEntity findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public UserEntity createUser(User user) {
        UserEntity userEntity = userMapper.userDTOtoDAO(user);
        return userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity updateUser(User newUser) {
        UserEntity oldUser = findById(newUser.getId());
        UserEntity updatedUserEntity = userMapper.updateUserDTOtoDAO(oldUser, newUser);
        return userRepository.save(updatedUserEntity);
    }

}
