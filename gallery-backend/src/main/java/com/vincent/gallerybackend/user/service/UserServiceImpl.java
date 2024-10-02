package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.event.repository.EventRepository;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.model.UserMapper;
import com.vincent.gallerybackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final EventRepository eventRepository;

    private final UserMapper userMapper;
    private final ArtworkMapper artworkMapper;

    @Transactional(readOnly = true)
    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userMapper.userDAOtoDTO(userEntity);
    }

    private UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Long> getFavoriteArtworkIdsByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userEntity.getFavoriteArtworkIds();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Long> getAcceptedEventIdsByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userEntity.getAcceptedEventIds();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Long> getNotificationIdsByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userEntity.getNotificationIds();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Artwork> getFavoriteArtworksByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        Set<Long> artworkIds = userEntity.getFavoriteArtworkIds();
        List<ArtworkEntity> artworkEntities = artworkRepository.findAllById(artworkIds);
        return artworkMapper.artworksDAOtoDTO(artworkEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Artwork> getRequestedArtworksByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        Set<Long> artworkIds = userEntity.getRequestedArtworkIds();
        List<ArtworkEntity> artworkEntities = artworkRepository.findAllById(artworkIds);
        return artworkMapper.artworksDAOtoDTO(artworkEntities);
    }

    @Transactional
    @Override
    public UserEntity createUser(User user) {
        UserEntity userEntity = userMapper.userDTOtoDAO(user);
        return userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public UserEntity updateUser(User newUser) {
        UserEntity oldUser = findByEmail(newUser.getEmail());
        UserEntity updatedUserEntity = userMapper.updateUserDTOtoDAO(oldUser, newUser);
        return userRepository.save(updatedUserEntity);
    }

    @Transactional
    @Override
    public UserEntity updateFavoriteArtworksOfUser(String email, long artworkId) {
        UserEntity userEntity = findByEmail(email);
        checkIfArtworkExists(artworkId);
        Set<Long> favoriteArtworks = addOrDeleteIdInSet(userEntity.getFavoriteArtworkIds(), artworkId);
        userEntity.setFavoriteArtworkIds(favoriteArtworks);
        return userRepository.save(userEntity);
    }

    private void checkIfArtworkExists(long artworkId) {
        boolean artworkExists = artworkRepository.existsById(artworkId);
        if (!artworkExists) {
            throw new NotFoundException("Artwork not found");
        }
    }

    @Transactional
    @Override
    public UserEntity updateAcceptedEventsOfUser(String email, long eventId) {
        UserEntity userEntity = findByEmail(email);
        checkIfEventExists(eventId);
        Set<Long> acceptedEvents = addOrDeleteIdInSet(userEntity.getAcceptedEventIds(), eventId);
        userEntity.setAcceptedEventIds(acceptedEvents);
        return userRepository.save(userEntity);
    }

    private void checkIfEventExists(long eventId) {
        boolean eventExists = eventRepository.existsById(eventId);
        if (!eventExists) {
            throw new NotFoundException("Event not found");
        }
    }

    private Set<Long> addOrDeleteIdInSet(Set<Long> set, long id) {
        boolean exists = set.contains(id);
        if (exists) {
            set.remove(id);
        } else {
            set.add(id);
        }
        return set;
    }

}
