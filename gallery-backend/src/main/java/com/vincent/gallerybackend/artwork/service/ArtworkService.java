package com.vincent.gallerybackend.artwork.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ArtworkService {

    Artwork getArtworkById(long id);

    List<Artwork> getArtworks();

    ArtworkEntity createArtwork(Artwork artwork, MultipartFile imageFile);

    ArtworkEntity updateArtwork(Artwork artwork, MultipartFile imageFile);

    void deleteArtwork(long id);

}
