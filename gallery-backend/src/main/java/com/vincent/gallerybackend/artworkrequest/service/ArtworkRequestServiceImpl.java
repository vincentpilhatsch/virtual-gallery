package com.vincent.gallerybackend.artworkrequest.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestEntity;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestMapper;
import com.vincent.gallerybackend.artworkrequest.model.CompleteArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.repository.ArtworkRequestRepository;
import com.vincent.gallerybackend.exceptions.ConflictException;
import com.vincent.gallerybackend.exceptions.InternalServerErrorException;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.model.UserMapper;
import com.vincent.gallerybackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtworkRequestServiceImpl implements ArtworkRequestService {

    private final ArtworkRequestRepository artworkRequestRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    private final ArtworkRequestMapper artworkRequestMapper;
    private final ArtworkMapper artworkMapper;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ArtworkRequest> getArtworkRequests() {
        List<ArtworkRequestEntity> artworkRequestEntities = artworkRequestRepository.findAll();
        return artworkRequestMapper.artworkRequestsDAOtoDTO(artworkRequestEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompleteArtworkRequest> getCompleteArtworkRequests() {
        List<ArtworkRequestEntity> artworkRequestEntities = artworkRequestRepository.findAll();
        List<CompleteArtworkRequest> completeArtworkRequests = new ArrayList<>();
        for (ArtworkRequestEntity artworkRequestEntity : artworkRequestEntities) {
            ArtworkRequest artworkRequest = artworkRequestMapper.artworkRequestDAOtoDTO(artworkRequestEntity);
            Artwork artwork = findArtworkById(artworkRequestEntity.getArtworkId());
            User user = findUserById(artworkRequestEntity.getUserId());
            CompleteArtworkRequest completeArtworkRequest = new CompleteArtworkRequest(artworkRequest, artwork, user);
            completeArtworkRequests.add(completeArtworkRequest);
        }
        return completeArtworkRequests;
    }

    private Artwork findArtworkById(long artworkId) {
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new NotFoundException("Artwork not found!"));
        return artworkMapper.artworkDAOtoDTO(artworkEntity);
    }

    private User findUserById(UUID userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        return userMapper.userDAOtoDTO(userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ArtworkRequest getArtworkRequestById(long id) {
        ArtworkRequestEntity artworkRequestEntity = findArtworkRequestById(id);
        return artworkRequestMapper.artworkRequestDAOtoDTO(artworkRequestEntity);
    }

    private ArtworkRequestEntity findArtworkRequestById(long id) {
        return artworkRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ArtworkRequest not found"));
    }

    @Transactional
    @Override
    public ArtworkRequest createArtworkRequest(ArtworkRequest artworkRequest) {
        ArtworkRequestEntity artworkRequestEntity = createArtworkRequestEntity(artworkRequest);
        artworkRequestEntity = artworkRequestRepository.save(artworkRequestEntity);
        setArtworkRequestForUser(artworkRequestEntity);
        return artworkRequestMapper.artworkRequestDAOtoDTO(artworkRequestEntity);
    }

    private ArtworkRequestEntity createArtworkRequestEntity(ArtworkRequest artworkRequest) {
        ArtworkRequestEntity artworkRequestEntity = artworkRequestMapper.artworkRequestDTOtoDAO(artworkRequest);
        artworkRequestEntity.setRequestTime(LocalDateTime.now());
        throwErrorIfArtworkRequestForUserAlreadyExists(artworkRequestEntity);
        return artworkRequestEntity;
    }

    private void throwErrorIfArtworkRequestForUserAlreadyExists(ArtworkRequestEntity artworkRequestEntity) {
        Set<Long> userArtworkRequestIds = getUsersArtworkRequestIds(artworkRequestEntity);
        boolean artworkExistsInUsersArtworkRequests = artworkExistsInUsersArtworkRequests(artworkRequestEntity, userArtworkRequestIds);
        if (artworkExistsInUsersArtworkRequests) {
            throw new ConflictException("An artwork request for the specified user already exists");
        }
    }

    private Set<Long> getUsersArtworkRequestIds(ArtworkRequestEntity artworkRequestEntity) {
        UserEntity userEntity = getUserFromArtworkRequest(artworkRequestEntity);
        return userEntity.getArtworkRequestIds();
    }

    private UserEntity getUserFromArtworkRequest(ArtworkRequestEntity artworkRequestEntity) {
        UUID userId = artworkRequestEntity.getUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private boolean artworkExistsInUsersArtworkRequests(ArtworkRequestEntity artworkRequestEntity,
                                                        Set<Long> userArtworkRequestIds) {
        List<ArtworkRequestEntity> artworkRequestEntities = artworkRequestRepository.findAllById(userArtworkRequestIds);
        Set<Long> usersArtworkIdsOfArtworkRequests =
                artworkRequestEntities
                        .stream()
                        .map(ArtworkRequestEntity::getArtworkId)
                        .collect(Collectors.toSet());
        long artworkId = artworkRequestEntity.getArtworkId();
        return usersArtworkIdsOfArtworkRequests.stream().anyMatch(element -> element == artworkId);
    }

    private void setArtworkRequestForUser(ArtworkRequestEntity artworkRequestEntity) {
        UserEntity userEntity = getUserFromArtworkRequest(artworkRequestEntity);
        Set<Long> newArtworkRequestIds = new HashSet<>(userEntity.getArtworkRequestIds());
        newArtworkRequestIds.add(artworkRequestEntity.getId());
        userEntity.setArtworkRequestIds(newArtworkRequestIds);
        userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public void acceptArtworkRequest(ArtworkRequest artworkRequest) {
        setArtworkToSold(artworkRequest.getArtworkId());
        artworkRequestRepository.deleteArtworkRequestEntitiesByArtworkId(artworkRequest.getArtworkId());
    }

    private void setArtworkToSold(long artworkId) {
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new NotFoundException("Artwork not found!"));
        artworkEntity.setSold(true);
        artworkRepository.save(artworkEntity);
    }

    @Transactional
    @Override
    public void rejectArtworkRequest(ArtworkRequest artworkRequest) {
        ArtworkRequestEntity artworkRequestEntity = artworkRequestMapper.artworkRequestDTOtoDAO(artworkRequest);
        artworkRequestRepository.delete(artworkRequestEntity);
    }

    @Transactional
    @Override
    public void deleteArtworkRequest(long id) {
        ArtworkRequestEntity artworkRequestEntity = findArtworkRequestById(id);
        artworkRequestRepository.delete(artworkRequestEntity);
    }

}
