package com.vincent.gallerybackend.artwork.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.config.FileValidationService;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ArtworkMapper artworkMapper;
    private final FileValidationService fileValidationService;

    @Transactional(readOnly = true)
    @Override
    @Cacheable("artworks")
    public List<Artwork> getArtworks() {
        List<ArtworkEntity> artworkEntities = artworkRepository.findAll();
        Collections.reverse(artworkEntities);
        return artworkMapper.artworksDAOtoDTO(artworkEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Artwork> getRandomArtworks(int numberOfArtworks) {
        List<ArtworkEntity> randomArtworks = pickRandomArtworks(numberOfArtworks);
        return artworkMapper.artworksDAOtoDTO(randomArtworks);
    }

    private List<ArtworkEntity> pickRandomArtworks(int numberOfArtworks) {
        List<ArtworkEntity> artworkEntities = artworkRepository.findAll();
        if (artworkEntities.size() < numberOfArtworks) {
            throw new IllegalArgumentException("Requested number of artworks (" + numberOfArtworks + ") " +
                    "exceeds the available artwork count (" + artworkEntities.size() + ")");
        }
        Collections.shuffle(artworkEntities);
        return artworkEntities.subList(0, numberOfArtworks);
    }

    @Transactional(readOnly = true)
    @Override
    public Artwork getArtworkById(long id) {
        ArtworkEntity artworkEntity = findById(id);
        return artworkMapper.artworkDAOtoDTO(artworkEntity);
    }

    private ArtworkEntity findById(long id) {
        return artworkRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artwork not found"));
    }

    @Transactional
    @CacheEvict(value = "artworks", allEntries = true)
    @Override
    public Artwork createArtwork(Artwork artwork, MultipartFile imageFile) {
        ArtworkEntity artworkEntity = artworkMapper.artworkDTOtoDAO(artwork);
        setArtworkImage(artworkEntity, imageFile);
        artworkEntity.setUploadedAt(LocalDateTime.now());
        artworkRepository.save(artworkEntity);
        return artworkMapper.artworkDAOtoDTO(artworkEntity);
    }

    @Transactional
    @CacheEvict(value = "artworks", allEntries = true)
    @Override
    public Artwork updateArtwork(Artwork newArtwork) {
        ArtworkEntity oldArtwork = findById(newArtwork.getId());
        ArtworkEntity updatedArtworkEntity = artworkMapper.updateArtworkDTOtoDAO(oldArtwork, newArtwork);
        updatedArtworkEntity.setUploadedAt(LocalDateTime.now());
        artworkRepository.save(updatedArtworkEntity);
        return artworkMapper.artworkDAOtoDTO(updatedArtworkEntity);
    }

    @Transactional
    @CacheEvict(value = "artworks", allEntries = true)
    @Override
    public Artwork updateArtwork(Artwork newArtwork, MultipartFile imageFile) {
        ArtworkEntity oldArtwork = findById(newArtwork.getId());
        ArtworkEntity updatedArtworkEntity = artworkMapper.updateArtworkDTOtoDAO(oldArtwork, newArtwork);
        setArtworkImage(updatedArtworkEntity, imageFile);
        updatedArtworkEntity.setUploadedAt(LocalDateTime.now());
        artworkRepository.save(updatedArtworkEntity);
        return artworkMapper.artworkDAOtoDTO(updatedArtworkEntity);
    }

    private void setArtworkImage(ArtworkEntity artworkEntity, MultipartFile imageFile) {
        fileValidationService.validateFile(imageFile);
        fileValidationService.safeImageInEntity(artworkEntity, imageFile);
    }

    @Transactional
    @CacheEvict(value = "artworks", allEntries = true)
    @Override
    public void deleteArtwork(long id) {
        ArtworkEntity artworkEntity = findById(id);
        artworkRepository.delete(artworkEntity);
    }

}
