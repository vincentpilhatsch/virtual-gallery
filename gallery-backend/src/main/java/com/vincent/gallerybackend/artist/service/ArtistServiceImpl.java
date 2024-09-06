package com.vincent.gallerybackend.artist.service;

import com.vincent.gallerybackend.artist.model.ArtistEntity;
import com.vincent.gallerybackend.artist.repository.ArtistRepository;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Transactional
    public boolean checkArtistExists(String email) {
        ArtistEntity artistEntity = artistRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Artist not found"));
        return artistEntity != null;
    }


}
