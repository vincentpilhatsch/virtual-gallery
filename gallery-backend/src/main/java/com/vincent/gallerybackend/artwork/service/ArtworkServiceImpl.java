package com.vincent.gallerybackend.artwork.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.exceptions.ImageFileException;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional(readOnly = true)
    public List<Artwork> getArtworks() {
        List<ArtworkEntity> artworkEntities = artworkRepository.findAll();
        return artworkMapper.artworksDAOtoDTO(artworkEntities);
    }

    @Transactional
    public ArtworkEntity createArtwork(Artwork artwork, MultipartFile imageFile) {
        ArtworkEntity artworkEntity = artworkMapper.artworkDTOtoDAO(artwork);
        setArtworkImage(artworkEntity, imageFile);
        artworkEntity.setUploadedAt(LocalDateTime.now());
        return artworkRepository.save(artworkEntity);
    }

    @Transactional
    public ArtworkEntity updateArtwork(Artwork newArtwork, MultipartFile imageFile) {
        ArtworkEntity oldArtwork = findById(newArtwork.getId());
        ArtworkEntity updatedArtworkEntity = artworkMapper.updateArtworkDTOtoDAO(oldArtwork, newArtwork);
        setArtworkImage(updatedArtworkEntity, imageFile);
        updatedArtworkEntity.setUploadedAt(LocalDateTime.now());
        return artworkRepository.save(updatedArtworkEntity);
    }

    // TODO: Wirklich notwendig?
    private void setArtworkImage(ArtworkEntity artworkEntity, MultipartFile imageFile) {
        try {
            if (imageFile == null) {
                return;
            }
            artworkEntity.setImage(imageFile.getBytes());
        } catch (IOException e) {
            throw new ImageFileException("Could not safe image in artwork");
        }
    }

    @Transactional
    public void deleteArtwork(long id) {
        ArtworkEntity artworkEntity = findById(id);
        artworkRepository.delete(artworkEntity);
    }

}
