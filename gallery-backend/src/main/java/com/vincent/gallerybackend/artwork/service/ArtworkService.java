package com.vincent.gallerybackend.artwork.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import org.springframework.stereotype.Service;

@Service
public interface ArtworkService {

    Artwork getArtworkById(long id);

    ArtworkEntity createArtwork(Artwork user);

    ArtworkEntity updateArtwork(Artwork user);

}
