package com.vincent.gallerybackend.artwork.service;

import com.vincent.gallerybackend.artwork.model.User;
import com.vincent.gallerybackend.artwork.model.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getUserById(String id);

    UserEntity createUser(User user);

    UserEntity updateUser(User user);

}
