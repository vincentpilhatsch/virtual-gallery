package com.vincent.gallerybackend.user.controller;

import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping(value = "/{email}", produces = "application/json")
    public User getUserById(@PathVariable String email) {
        return userServiceImpl.getUserByEmail(email);
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
