package com.vincent.gallerybackend.artwork.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
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
    public ResponseEntity<List<Artwork>> getArtworks() {
        List<Artwork> artworks = artworkServiceImpl.getArtworks();
        log.info("GET ALL artworks ({})", artworks.size());
        // TODO: Browser-Caching (HTTP)
        //        return ResponseEntity.ok()
        //        .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic())
        //        .body(artworks);
        return ResponseEntity.ok().body(artworks);
    }

    @GetMapping(value = "/random", produces = "application/json")
    public ResponseEntity<List<Artwork>> getRandomArtworks(@RequestParam int numberOfArtworks) {
        List<Artwork> artworks = artworkServiceImpl.getRandomArtworks(numberOfArtworks);
        log.info("GET ALL random artworks ({})", artworks.size());
        return new ResponseEntity<>(artworks, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Artwork> getArtwork(@PathVariable long id) {
        Artwork artwork = artworkServiceImpl.getArtworkById(id);
        log.info("GET artwork with id: {}", artwork.getId());
        return new ResponseEntity<>(artwork, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Artwork> createArtwork(@Valid @RequestPart("artwork") Artwork artwork,
                                                 @RequestPart("file") MultipartFile imageFile) {
        Artwork createdEntity = artworkServiceImpl.createArtwork(artwork, imageFile);
        log.info("CREATE artwork with id: {}", createdEntity.getId());
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping(value = "update", produces = "application/json")
    public ResponseEntity<Artwork> updateArtwork(@Valid @RequestBody Artwork newArtwork) {
        Artwork updatedArtwork = artworkServiceImpl.updateArtwork(newArtwork);
        log.info("UPDATE artwork with id: {}", updatedArtwork.getId());
        return new ResponseEntity<>(updatedArtwork, HttpStatus.OK);
    }

    @PutMapping(value = "updateWithImage", produces = "application/json")
    public ResponseEntity<Artwork> updateArtwork(@Valid @RequestPart("artwork") Artwork newArtwork,
                                                 @RequestPart("file") MultipartFile imageFile) {
        Artwork updatedArtwork = artworkServiceImpl.updateArtwork(newArtwork, imageFile);
        log.info("UPDATE artwork with image with id: {}", updatedArtwork.getId());
        return new ResponseEntity<>(updatedArtwork, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable long id) {
        artworkServiceImpl.deleteArtwork(id);
        log.info("DELETE artwork with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
