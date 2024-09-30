package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {

    User getUserByEmail(String id);

    Set<Long> getFavoriteArtworkIdsByEmail(String id);

    Set<Long> getAcceptedEventIdsByEmail(String id);

    List<Artwork> getFavoriteArtworksByEmail(String id);

    Set<Long> getNotificationIdsByEmail(String id);

    UserEntity createUser(User user);

    UserEntity updateUser(User user);

    UserEntity updateFavoriteArtworksOfUser(String email, long artworkId);

    UserEntity updateAcceptedEventsOfUser(String email, long eventId);

}
