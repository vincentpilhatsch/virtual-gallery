package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.user.model.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {

    User getUserByEmail(String email);

    User getUserByToken(String token);

    Set<Long> getFavoriteArtworkIdsByEmail(String email);

    Set<Long> getAcceptedEventIdsByEmail(String email);

    Set<Long> getNotificationIdsByEmail(String email);

    List<Artwork> getFavoriteArtworksByEmail(String email);

    List<Artwork> getRequestedArtworksByEmail(String email);

    ResponseEntity<String> createUser(User user);

    AuthResponse loginUser(LoginRequest loginRequest, HttpServletResponse response);

    UserEntity updateUser(User user);

    UserEntity updateLanguageOfUser(String email, Language language);

    UserEntity updateFavoriteArtworksOfUser(String email, long artworkId);

    UserEntity updateAcceptedEventsOfUser(String email, long eventId);

    UserEntity updateOneRequestedArtworkOfUser(String email, long eventId);

}
