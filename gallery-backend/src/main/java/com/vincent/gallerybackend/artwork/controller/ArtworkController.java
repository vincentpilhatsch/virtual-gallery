package com.vincent.gallerybackend.artwork.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.service.ArtworkServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/artwork")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkServiceImpl artworkServiceImpl;

    @GetMapping(value = "/all", produces = "application/json")
    public List<Artwork> getArtworks() {
        List<Artwork> artworks = artworkServiceImpl.getArtworks();
        log.info("GET ALL artworks ({}): {}", artworks.size(), artworks);
        return artworks;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Artwork getArtworkById(@PathVariable long id) {
        Artwork artwork = artworkServiceImpl.getArtworkById(id);
        log.info("GET artwork: {}", artwork);
        return artwork;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ArtworkEntity> createArtwork(@Valid @RequestPart("artwork") Artwork artwork,
                                                       @RequestPart("file") MultipartFile imageFile) {
        ArtworkEntity artworkEntity = artworkServiceImpl.createArtwork(artwork, imageFile);
        log.info("CREATE artwork: {}", artwork);
        return new ResponseEntity<>(artworkEntity, HttpStatus.CREATED);
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<ArtworkEntity> updateArtwork(@Valid @RequestPart("artwork") Artwork newArtwork,
                                                       @RequestPart("file") MultipartFile imageFile) {
        ArtworkEntity updatedArtworkEntity = artworkServiceImpl.updateArtwork(newArtwork, imageFile);
        log.info("UPDATE artwork: {}", newArtwork);
        return new ResponseEntity<>(updatedArtworkEntity, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable long id) {
        artworkServiceImpl.deleteArtwork(id);
        log.info("DELETE artwork with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
