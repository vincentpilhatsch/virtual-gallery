package com.vincent.gallerybackend.user.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping(path = "/user")
//@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping(value = "/{email}", produces = "application/json")
    public User getUser(@PathVariable String email) {
        User user = userServiceImpl.getUserByEmail(email);
        log.info("GET user: {}", user.getEmail());
        return user;
    }

    @GetMapping(value = "/favoriteArtworkIds/{email}", produces = "application/json")
    public Set<Long> getFavoriteArtworkIds(@PathVariable String email) {
        Set<Long> favoriteArtworkIds = userServiceImpl.getFavoriteArtworkIdsByEmail(email);
        log.info("GET favorite artwork ids: {}", favoriteArtworkIds);
        return favoriteArtworkIds;
    }

    @GetMapping(value = "/acceptedEventIds/{email}", produces = "application/json")
    public Set<Long> getAcceptedEventIds(@PathVariable String email) {
        Set<Long> acceptedEventIds = userServiceImpl.getAcceptedEventIdsByEmail(email);
        log.info("GET accepted event ids: {}", acceptedEventIds);
        return acceptedEventIds;
    }

    @GetMapping(value = "/notificationIds/{email}", produces = "application/json")
    public Set<Long> getNotificationIds(@PathVariable String email) {
        Set<Long> notificationIds = userServiceImpl.getNotificationIdsByEmail(email);
        log.info("GET notification ids: {}", notificationIds);
        return notificationIds;
    }

    @GetMapping(value = "/favoriteArtworks/{email}", produces = "application/json")
    public List<Artwork> getFavoriteArtworks(@PathVariable String email) {
        List<Artwork> favoriteArtworks = userServiceImpl.getFavoriteArtworksByEmail(email);
        log.info("GET favorite artworks ({})", favoriteArtworks.size());
        return favoriteArtworks;
    }

    @GetMapping(value = "/requestedArtworks/{email}", produces = "application/json")
    public List<Artwork> getRequestedArtworks(@PathVariable String email) {
        List<Artwork> requestedArtworks = userServiceImpl.getRequestedArtworksByEmail(email);
        log.info("GET requested artworks ({})", requestedArtworks.size());
        return requestedArtworks;
    }

    @PostMapping(value = "/updateOneRequestedArtwork")
    public ResponseEntity<UserEntity> updateOneRequestedArtworkOfUser(@RequestParam String email, @RequestParam long artworkId) {
        UserEntity userEntity = userServiceImpl.updateOneRequestedArtworkOfUser(email, artworkId);
        log.info("UPDATE one requested artwork for user: {}", userEntity.getEmail());
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PostMapping(value = "/updateFavoriteArtworks")
    public ResponseEntity<UserEntity> updateFavoriteArtworksOfUser(@RequestParam String email, @RequestParam long artworkId) {
        UserEntity userEntity = userServiceImpl.updateFavoriteArtworksOfUser(email, artworkId);
        log.info("UPDATE favorite artwork for user: {}", userEntity.getEmail());
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PostMapping(value = "/updateAcceptedEvents")
    public ResponseEntity<UserEntity> updateAcceptedEventsOfUser(@RequestParam String email, @RequestParam long eventId) {
        UserEntity userEntity = userServiceImpl.updateAcceptedEventsOfUser(email, eventId);
        log.info("UPDATE favorite artwork for user: {}", userEntity.getEmail());
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserEntity> updateUser(@Valid @RequestBody User newUser) {
        UserEntity updatedUserEntity = userServiceImpl.updateUser(newUser);
        log.info("UPDATE user: {}", newUser.getEmail());
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        ResponseEntity<?> responseEntity = userServiceImpl.createUser(user);
        log.info("REGISTER user: {}, {}", user.getEmail(), responseEntity.getBody());
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        ResponseEntity<?> responseEntity = userServiceImpl.loginUser(user);
        log.info("LOGIN user: {}, {}", user.getEmail(), responseEntity.getBody());
        return responseEntity;
    }

}
