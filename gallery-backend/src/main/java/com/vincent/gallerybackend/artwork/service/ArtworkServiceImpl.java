package com.vincent.gallerybackend.artwork.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ArtworkMapper artworkMapper;

    @Transactional(readOnly = true)
    public Artwork getArtworkById(long id) {
        ArtworkEntity artworkEntity = findById(id);
        return artworkMapper.artworkDAOtoDTO(artworkEntity);
    }

    private ArtworkEntity findById(long id) {
        return artworkRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artwork not found"));
    }

    @Transactional
    public ArtworkEntity createArtwork(Artwork artwork) {
        boolean exists = artworkRepository.existsById(artwork.getId());
        if (exists) {
            throw new IllegalStateException("Artwork already exists");
        }

        ArtworkEntity artworkEntity = artworkMapper.artworkDTOtoDAO(artwork);
        return artworkRepository.save(artworkEntity);
    }

    @Transactional
    public ArtworkEntity updateArtwork(Artwork newArtwork) {
        ArtworkEntity oldArtwork = findById(newArtwork.getId());
        ArtworkEntity updatedArtworkEntity = artworkMapper.updateArtworkDTOtoDAO(oldArtwork, newArtwork);
        return artworkRepository.save(updatedArtworkEntity);
    }

}
