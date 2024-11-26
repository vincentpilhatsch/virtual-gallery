package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkAndArtworkRequest;
import com.vincent.gallerybackend.user.model.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {

    User getCurrentUser(Authentication authentication);

    User getUserByEmail(String email);

    User getUserByToken(String token);

    Set<Long> getFavoriteArtworkIdsByEmail(String email);

    Set<Long> getAcceptedEventIdsByEmail(String email);

    Set<Long> getNotificationIdsByEmail(String email);

    List<Artwork> getFavoriteArtworksByEmail(String email);

    List<ArtworkAndArtworkRequest> getArtworksAndArtworkRequestsOfUser(String email);

    AuthResponse loginUser(LoginRequest loginRequest, HttpServletResponse response);

    AuthResponse registerUser(RegisterRequest registerRequest, HttpServletResponse response);

    void logoutUser(HttpServletResponse response);

    User updateUser(User user);

    User updateLanguageOfUser(String email, Language language);

    User updateFavoriteArtworksOfUser(String email, long artworkId);

    User updateAcceptedEventsOfUser(String email, long eventId);

    User updateOneRequestedArtworkOfUser(String email, long eventId);

}
