package com.vincent.gallerybackend.user.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.security.JwtUtils;
import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping(path = "/user")
//@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping(value = "/{email}", produces = "application/json")
    public User getUser(@PathVariable String email) {
        User user = userServiceImpl.getUserByEmail(email);
        log.info("GET user: {}", user);
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

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody User user) {
        UserEntity userEntity = userServiceImpl.createUser(user);
        log.info("CREATE user: {}", user);
        return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
    }

    @PostMapping(value = "/updateOneRequestedArtwork")
    public ResponseEntity<UserEntity> updateOneRequestedArtworkOfUser(@RequestParam String email, @RequestParam long artworkId) {
        UserEntity userEntity = userServiceImpl.updateOneRequestedArtworkOfUser(email, artworkId);
        log.info("UPDATE one requested artwork for user: {}", userEntity);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PostMapping(value = "/updateFavoriteArtworks")
    public ResponseEntity<UserEntity> updateFavoriteArtworksOfUser(@RequestParam String email, @RequestParam long artworkId) {
        UserEntity userEntity = userServiceImpl.updateFavoriteArtworksOfUser(email, artworkId);
        log.info("UPDATE favorite artwork for user: {}", userEntity);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PostMapping(value = "/updateAcceptedEvents")
    public ResponseEntity<UserEntity> updateAcceptedEventsOfUser(@RequestParam String email, @RequestParam long eventId) {
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userServiceImpl.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        userServiceImpl.createUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails);
            return ResponseEntity.ok(jwt);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

}
