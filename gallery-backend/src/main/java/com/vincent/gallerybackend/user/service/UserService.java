package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {

    User getUserByEmail(String email);

    Set<Long> getFavoriteArtworkIdsByEmail(String email);

    Set<Long> getAcceptedEventIdsByEmail(String email);

    Set<Long> getNotificationIdsByEmail(String email);

    List<Artwork> getFavoriteArtworksByEmail(String email);

    List<Artwork> getRequestedArtworksByEmail(String email);

    UserEntity createUser(User user);

    UserEntity updateUser(User user);

    UserEntity updateFavoriteArtworksOfUser(String email, long artworkId);

    UserEntity updateAcceptedEventsOfUser(String email, long eventId);

}
