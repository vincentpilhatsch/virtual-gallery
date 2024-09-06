package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.model.UserMapper;
import com.vincent.gallerybackend.user.repository.UserRepository;
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
    public User getUserByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userMapper.userDAOtoDTO(userEntity);
    }


    private UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public UserEntity createUser(User user) {
        UserEntity userEntity = userMapper.userDTOtoDAO(user);
        return userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity updateUser(User newUser) {
        UserEntity oldUser = findByEmail(newUser.getEmail());
        UserEntity updatedUserEntity = userMapper.updateUserDTOtoDAO(oldUser, newUser);
        return userRepository.save(updatedUserEntity);
    }

}
