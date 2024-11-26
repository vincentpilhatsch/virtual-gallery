package com.vincent.gallerybackend.user.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkAndArtworkRequest;
import com.vincent.gallerybackend.user.model.*;
import com.vincent.gallerybackend.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            return ResponseEntity.noContent().build();
        }
        User user = userServiceImpl.getCurrentUser(authentication);
        log.info("GET me: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/{email}", produces = "application/json")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userServiceImpl.getUserByEmail(email);
        log.info("GET user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<User> getUserByToken(@RequestBody String token) {
        User user = userServiceImpl.getUserByToken(token);
        log.info("GET user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/favoriteArtworkIds/{email}", produces = "application/json")
    public ResponseEntity<Set<Long>> getFavoriteArtworkIds(@PathVariable String email) {
        Set<Long> favoriteArtworkIds = userServiceImpl.getFavoriteArtworkIdsByEmail(email);
        log.info("GET favorite artwork ids: {}", favoriteArtworkIds);
        return new ResponseEntity<>(favoriteArtworkIds, HttpStatus.OK);
    }

    @GetMapping(value = "/acceptedEventIds/{email}", produces = "application/json")
    public ResponseEntity<Set<Long>> getAcceptedEventIds(@PathVariable String email) {
        Set<Long> acceptedEventIds = userServiceImpl.getAcceptedEventIdsByEmail(email);
        log.info("GET accepted event ids: {}", acceptedEventIds);
        return new ResponseEntity<>(acceptedEventIds, HttpStatus.OK);
    }

    @GetMapping(value = "/notificationIds/{email}", produces = "application/json")
    public ResponseEntity<Set<Long>> getNotificationIds(@PathVariable String email) {
        Set<Long> notificationIds = userServiceImpl.getNotificationIdsByEmail(email);
        log.info("GET notification ids: {}", notificationIds);
        return new ResponseEntity<>(notificationIds, HttpStatus.OK);
    }

    @GetMapping(value = "/favoriteArtworks/{email}", produces = "application/json")
    public ResponseEntity<List<Artwork>> getFavoriteArtworks(@PathVariable String email) {
        List<Artwork> favoriteArtworks = userServiceImpl.getFavoriteArtworksByEmail(email);
        log.info("GET favorite artworks ({})", favoriteArtworks.size());
        return new ResponseEntity<>(favoriteArtworks, HttpStatus.OK);
    }

    @GetMapping(value = "/artworksAndArtworkRequests/{email}", produces = "application/json")
    public ResponseEntity<List<ArtworkAndArtworkRequest>> getArtworksAndArtworkRequestsOfUser(
            @PathVariable String email
    ) {
        List<ArtworkAndArtworkRequest> artworksAndArtworkRequests =
                userServiceImpl.getArtworksAndArtworkRequestsOfUser(email);
        log.info("GET artwork and artwork requests ({})", artworksAndArtworkRequests.size());
        return new ResponseEntity<>(artworksAndArtworkRequests, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest,
                                                  HttpServletResponse response) {
        AuthResponse responseEntity = userServiceImpl.loginUser(loginRequest, response);
        log.info("LOGIN user: {}", loginRequest.getEmail());
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest registerRequest,
                                                     HttpServletResponse response) {
        AuthResponse responseEntity = userServiceImpl.registerUser(registerRequest, response);
        log.info("REGISTER user: {}", registerRequest.getEmail());
        return new ResponseEntity<>(responseEntity, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        userServiceImpl.logoutUser(response);
        log.info("LOGOUT user");
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser) {
        User updatedUser = userServiceImpl.updateUser(newUser);
        log.info("UPDATE user: {}", newUser.getEmail());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping(value = "/updateLanguage")
    public ResponseEntity<User> updateLanguageOfUser(@RequestParam String email,
                                                     @RequestParam Language language) {
        User updatedUser = userServiceImpl.updateLanguageOfUser(email, language);
        log.info("UPDATE language for user: {}", updatedUser.getEmail());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping(value = "/updateFavoriteArtworks")
    public ResponseEntity<User> updateFavoriteArtworksOfUser(@RequestParam String email,
                                                             @RequestParam long artworkId) {
        User user = userServiceImpl.updateFavoriteArtworksOfUser(email, artworkId);
        log.info("UPDATE favorite artwork for user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/updateAcceptedEvents")
    public ResponseEntity<User> updateAcceptedEventsOfUser(@RequestParam String email,
                                                           @RequestParam long eventId) {
        User user = userServiceImpl.updateAcceptedEventsOfUser(email, eventId);
        log.info("UPDATE accepted for user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/updateOneRequestedArtwork")
    public ResponseEntity<User> updateOneRequestedArtworkOfUser(@RequestParam String email,
                                                                @RequestParam long artworkId) {
        User user = userServiceImpl.updateOneRequestedArtworkOfUser(email, artworkId);
        log.info("UPDATE one requested artwork for user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
