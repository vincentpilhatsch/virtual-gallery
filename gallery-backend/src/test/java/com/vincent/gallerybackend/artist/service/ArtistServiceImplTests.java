package com.vincent.gallerybackend.artist.service;

import com.vincent.gallerybackend.artist.model.Artist;
import com.vincent.gallerybackend.artist.model.ArtistEntity;
import com.vincent.gallerybackend.artist.model.Language;
import com.vincent.gallerybackend.artist.repository.ArtistRepository;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.repository.UserRepository;
import com.vincent.gallerybackend.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistServiceImplTests {

    private Artist artist;
    private ArtistEntity artistEntity;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistServiceImpl artistServiceImpl;

    @BeforeEach
    void setup() {
        artist = new Artist(UUID.randomUUID(),
                "müller@bht.de",
                "müller123",
                Language.DE);
        artistEntity = new ArtistEntity(UUID.randomUUID(),
                "müller@bht.de",
                "müller123",
                Language.DE);
        artistRepository.save(artistEntity);
    }

    // checkArtistExists

    @Test
    void checkArtistExists() {
        when(artistRepository.findByEmail(artistEntity.getEmail())).thenReturn(Optional.ofNullable(artistEntity));

        boolean artistExists = artistServiceImpl.checkArtistExists(artistEntity.getEmail());

        verify(artistRepository).findByEmail(artistEntity.getEmail());
        assertTrue(artistExists);
    }

    @Test
    void checkArtistExistsShouldThrowNotFound() {
        when(artistRepository.findByEmail("notexisting@bht.de"))
                .thenThrow(new NotFoundException("Artist not found"));

        assertThrows(NotFoundException.class, () ->
                artistServiceImpl.checkArtistExists("notexisting@bht.de"));
    }

}
