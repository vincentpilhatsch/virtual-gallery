package com.vincent.gallerybackend.artwork.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ArtworkService {

    Artwork getArtworkById(long id);

    List<Artwork> getArtworks();

    List<Artwork> getRandomArtworks(int numberOfArtworks);

    Artwork createArtwork(Artwork artwork, MultipartFile imageFile);

    Artwork updateArtwork(Artwork artwork, MultipartFile imageFile);

    void deleteArtwork(long id);

}
