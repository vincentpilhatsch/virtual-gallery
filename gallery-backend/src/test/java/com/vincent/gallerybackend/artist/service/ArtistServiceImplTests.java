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
        artist = new Artist("0",
                "müller@bht.de",
                "müller123",
                Language.DE);
        artistEntity = new ArtistEntity("0",
                "müller@bht.de",
                "müller123",
                Language.DE);
    }

    // checkArtistExists

    @Test
    void checkArtistExists() {
        when(artistRepository.findById(artist.getId())).thenReturn(Optional.ofNullable(artistEntity));

        boolean artistExists = artistServiceImpl.checkArtistExists(artist.getId());

        verify(artistRepository).findById(artist.getId());
        assertTrue(artistExists);
    }

    @Test
    void checkArtistExistsShouldThrowNotFound() {
        when(artistRepository.findById("1111"))
                .thenThrow(new NotFoundException("User not found"));

        assertThrows(NotFoundException.class, () ->
                artistServiceImpl.checkArtistExists("1111"));
    }

}
