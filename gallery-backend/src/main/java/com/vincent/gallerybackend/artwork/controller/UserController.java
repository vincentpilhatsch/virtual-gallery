package com.vincent.gallerybackend.artwork.controller;

import com.vincent.gallerybackend.artwork.model.User;
import com.vincent.gallerybackend.artwork.model.UserEntity;
import com.vincent.gallerybackend.artwork.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping(value = "/{id}", produces = "application/json")
    public User getUserById(@PathVariable String id) {
        return userServiceImpl.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody User user) {
        UserEntity userEntity = userServiceImpl.createUser(user);
        return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserEntity> updateUser(@RequestBody User newUser) {
        UserEntity updatedUserEntity = userServiceImpl.updateUser(newUser);
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

}
