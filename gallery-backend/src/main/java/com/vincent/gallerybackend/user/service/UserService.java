package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getUserById(String id);

    UserEntity createUser(User user);

    UserEntity updateUser(User user);

}
