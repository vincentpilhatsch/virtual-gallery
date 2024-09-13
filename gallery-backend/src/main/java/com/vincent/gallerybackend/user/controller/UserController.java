package com.vincent.gallerybackend.user.controller;

import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping(value = "/{email}", produces = "application/json")
    public User getUserById(@PathVariable String email) {
        User user = userServiceImpl.getUserByEmail(email);
        log.info("GET user: {}", user);
        return user;
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody User user) {
        UserEntity userEntity = userServiceImpl.createUser(user);
        log.info("CREATE user: {}", user);
        return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
    }

    @PostMapping(value = "/updateFavoriteArtworks")
    public ResponseEntity<UserEntity> updateFavoriteArtworksOfUser(
            @RequestParam String email,
            @RequestParam long artworkId) {
        UserEntity userEntity = userServiceImpl.updateFavoriteArtworksOfUser(email, artworkId);
        log.info("UPDATE favorite artwork for user: {}", userEntity);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PostMapping(value = "/updateAcceptedEvents")
    public ResponseEntity<UserEntity> updateAcceptedEventsOfUser(
            @RequestParam String email,
            @RequestParam long eventId) {
        UserEntity userEntity = userServiceImpl.updateAcceptedEventsOfUser(email, eventId);
        log.info("UPDATE favorite artwork for user: {}", userEntity);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserEntity> updateUser(@Valid @RequestBody User newUser) {
        UserEntity updatedUserEntity = userServiceImpl.updateUser(newUser);
        log.info("UPDATE user: {}", newUser);
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

}
