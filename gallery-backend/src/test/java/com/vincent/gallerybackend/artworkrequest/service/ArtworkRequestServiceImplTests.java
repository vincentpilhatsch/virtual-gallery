package com.vincent.gallerybackend.artworkrequest.service;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestEntity;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestMapper;
import com.vincent.gallerybackend.artworkrequest.repository.ArtworkRequestRepository;
import com.vincent.gallerybackend.exceptions.InternalServerErrorException;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ArtworkRequestServiceImplTests {

    @InjectMocks
    private ArtworkRequestServiceImpl artworkRequestServiceImpl;

    @Mock
    private ArtworkRequestRepository artworkRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArtworkRepository artworkRepository;

    @Mock
    private ArtworkRequestMapper artworkRequestMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createArtworkRequestShouldSaveAndReturnArtworkRequest() {
        UUID userId = UUID.randomUUID();
        ArtworkRequest artworkRequest = new ArtworkRequest(
                0L,
                "Test request",
                null,
                userId,
                1L);
        ArtworkRequestEntity artworkRequestEntity = new ArtworkRequestEntity(
                0L,
                "Test request",
                LocalDateTime.now(),
                userId,
                1L);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setArtworkRequestIds(Set.of());

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(artworkRequestMapper.artworkRequestDTOtoDAO(artworkRequest)).thenReturn(artworkRequestEntity);
        when(artworkRequestRepository.save(any(ArtworkRequestEntity.class))).thenReturn(artworkRequestEntity);
        when(artworkRequestMapper.artworkRequestDAOtoDTO(artworkRequestEntity)).thenReturn(artworkRequest);

        ArtworkRequest result = artworkRequestServiceImpl.createArtworkRequest(artworkRequest);

        verify(artworkRequestRepository, times(1)).save(artworkRequestEntity);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertEquals(artworkRequest.getDescription(), result.getDescription());
    }

    @Test
    void createArtworkRequestShouldThrowErrorIfRequestForArtworkExists() {
        UUID userId = UUID.randomUUID();
        ArtworkRequest artworkRequest = new ArtworkRequest(
                0L,
                "Test request",
                null,
                userId,
                1L);
        ArtworkRequestEntity artworkRequestEntity = new ArtworkRequestEntity(
                0L,
                "Test request",
                LocalDateTime.now(),
                userId,
                1L);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setArtworkRequestIds(Set.of(1L));

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(artworkRequestMapper.artworkRequestDTOtoDAO(artworkRequest)).thenReturn(artworkRequestEntity);
        when(artworkRequestRepository.findAllById(userEntity.getArtworkRequestIds()))
                .thenReturn(List.of(artworkRequestEntity));

        assertThrows(InternalServerErrorException.class, () -> artworkRequestServiceImpl.createArtworkRequest(artworkRequest));
    }


    @Test
    void acceptArtworkRequestShouldMarkArtworkAsSoldAndDeleteRequests() {
        long artworkId = 1L;
        ArtworkRequest artworkRequest = new ArtworkRequest(
                0L,
                "Accept request",
                null,
                UUID.randomUUID(),
                artworkId);
        ArtworkEntity artworkEntity = new ArtworkEntity();
        artworkEntity.setId(artworkId);
        artworkEntity.setSold(false);

        when(artworkRepository.findById(artworkId)).thenReturn(Optional.of(artworkEntity));

        artworkRequestServiceImpl.acceptArtworkRequest(artworkRequest);

        verify(artworkRepository, times(1)).save(artworkEntity);
        verify(artworkRequestRepository, times(1)).deleteArtworkRequestEntitiesByArtworkId(artworkId);
        assertTrue(artworkEntity.isSold());
    }

    @Test
    void acceptArtworkRequestShouldThrowIfArtworkNotFound() {
        long artworkId = 1L;
        ArtworkRequest artworkRequest = new ArtworkRequest(
                0L,
                "Accept request",
                null,
                UUID.randomUUID(),
                artworkId);

        when(artworkRepository.findById(artworkId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> artworkRequestServiceImpl.acceptArtworkRequest(artworkRequest));
    }

    @Test
    void rejectArtworkRequestShouldDeleteRequest() {
        ArtworkRequest artworkRequest = new ArtworkRequest(
                0L,
                "Reject request",
                null,
                UUID.randomUUID(),
                1L);
        ArtworkRequestEntity artworkRequestEntity = new ArtworkRequestEntity(
                0L,
                "Reject request",
                LocalDateTime.now(),
                artworkRequest.getUserId(),
                artworkRequest.getArtworkId());

        when(artworkRequestMapper.artworkRequestDTOtoDAO(artworkRequest)).thenReturn(artworkRequestEntity);

        artworkRequestServiceImpl.rejectArtworkRequest(artworkRequest);

        verify(artworkRequestRepository, times(1)).delete(artworkRequestEntity);
    }
}
