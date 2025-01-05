package com.vincent.gallerybackend.user.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkAndArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import com.vincent.gallerybackend.user.model.*;
import com.vincent.gallerybackend.user.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Retrieve current user", description = "Fetches the details of the currently authenticated user based on their session or token.")
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

    @Operation(summary = "Retrieve user by email", description = "Fetches the details of a user using their email address.")
    @GetMapping(value = "/{email}", produces = "application/json")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userServiceImpl.getUserByEmail(email);
        log.info("GET user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve user by token", description = "Fetches user details based on a provided token.")
    @PostMapping()
    public ResponseEntity<User> getUserByToken(@RequestBody String token) {
        User user = userServiceImpl.getUserByToken(token);
        log.info("GET user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve favorite artwork IDs", description = "Fetches a set of IDs representing the artworks marked as favorite by the user.")
    @GetMapping(value = "/favoriteArtworkIds/{email}", produces = "application/json")
    public ResponseEntity<Set<Long>> getFavoriteArtworkIds(@PathVariable String email) {
        Set<Long> favoriteArtworkIds = userServiceImpl.getFavoriteArtworkIdsByEmail(email);
        log.info("GET favorite artwork ids: {}", favoriteArtworkIds);
        return new ResponseEntity<>(favoriteArtworkIds, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve accepted event IDs", description = "Fetches a set of event IDs that the user has accepted.")
    @GetMapping(value = "/acceptedEventIds/{email}", produces = "application/json")
    public ResponseEntity<Set<Long>> getAcceptedEventIds(@PathVariable String email) {
        Set<Long> acceptedEventIds = userServiceImpl.getAcceptedEventIdsByEmail(email);
        log.info("GET accepted event ids: {}", acceptedEventIds);
        return new ResponseEntity<>(acceptedEventIds, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve favorite artworks", description = "Fetches a list of artworks marked as favorite by the user.")
    @GetMapping(value = "/favoriteArtworks/{email}", produces = "application/json")
    public ResponseEntity<List<Artwork>> getFavoriteArtworks(@PathVariable String email) {
        List<Artwork> favoriteArtworks = userServiceImpl.getFavoriteArtworksByEmail(email);
        log.info("GET favorite artworks ({})", favoriteArtworks.size());
        return new ResponseEntity<>(favoriteArtworks, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve artworks and artwork requests", description = "Fetches a list of artworks and artwork requests associated with the user.")
    @GetMapping(value = "/artworksAndArtworkRequests/{email}", produces = "application/json")
    public ResponseEntity<List<ArtworkAndArtworkRequest>> getArtworksAndArtworkRequests(
            @PathVariable String email
    ) {
        List<ArtworkAndArtworkRequest> artworksAndArtworkRequests =
                userServiceImpl.getArtworksAndArtworkRequestsOfUser(email);
        log.info("GET artwork and artwork requests ({})", artworksAndArtworkRequests.size());
        return new ResponseEntity<>(artworksAndArtworkRequests, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve artwork requests", description = "Fetches a list of artwork requests made by the user.")
    @GetMapping(value = "/artworkRequests/{email}", produces = "application/json")
    public ResponseEntity<List<ArtworkRequest>> getArtworkRequests(@PathVariable String email) {
        List<ArtworkRequest> artworkRequests = userServiceImpl.getArtworkRequestsByEmail(email);
        log.info("GET artwork requests for user ({})", artworkRequests.size());
        return new ResponseEntity<>(artworkRequests, HttpStatus.OK);
    }

    @Operation(summary = "Authenticate user", description = "Authenticates the user using their email and password and returns an authentication response.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest,
                                                  HttpServletResponse response) {
        AuthResponse responseEntity = userServiceImpl.loginUser(loginRequest, response);
        log.info("LOGIN user: {}", loginRequest.getEmail());
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details and returns an authentication response.")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest registerRequest,
                                                     HttpServletResponse response) {
        AuthResponse responseEntity = userServiceImpl.registerUser(registerRequest, response);
        log.info("REGISTER user: {}", registerRequest.getEmail());
        return new ResponseEntity<>(responseEntity, HttpStatus.CREATED);
    }

    @Operation(summary = "Logout user", description = "Logs out the user and clears their authentication cookies or session.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        userServiceImpl.logoutUser(response);
        log.info("LOGOUT user");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update user details", description = "Updates the details of an existing user with the provided data.")
    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser) {
        User updatedUser = userServiceImpl.updateUser(newUser);
        log.info("UPDATE user: {}", newUser.getEmail());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Update user language", description = "Updates the preferred language of the user.")
    @PutMapping(value = "/updateLanguage")
    public ResponseEntity<User> updateLanguageOfUser(@RequestParam String email,
                                                     @RequestParam Language language) {
        User updatedUser = userServiceImpl.updateLanguageOfUser(email, language);
        log.info("UPDATE language for user: {}", updatedUser.getEmail());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Update favorite artworks", description = "Adds or removes an artwork from the user's list of favorite artworks.")
    @PostMapping(value = "/updateFavoriteArtworks")
    public ResponseEntity<User> updateFavoriteArtworksOfUser(@RequestParam String email,
                                                             @RequestParam long artworkId) {
        User user = userServiceImpl.updateFavoriteArtworksOfUser(email, artworkId);
        log.info("UPDATE favorite artwork for user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Update accepted events", description = "Adds or removes an event from the user's list of accepted events.")
    @PostMapping(value = "/updateAcceptedEvents")
    public ResponseEntity<User> updateAcceptedEventsOfUser(@RequestParam String email,
                                                           @RequestParam long eventId) {
        User user = userServiceImpl.updateAcceptedEventsOfUser(email, eventId);
        log.info("UPDATE accepted for user: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
