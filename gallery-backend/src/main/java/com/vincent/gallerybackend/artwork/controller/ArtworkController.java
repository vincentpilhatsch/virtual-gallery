package com.vincent.gallerybackend.artwork.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.service.ArtworkServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Retrieve all artworks", description = "Fetches a list of all artworks available in the system.")
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Artwork>> getArtworks() {
        List<Artwork> artworks = artworkServiceImpl.getArtworks();
        log.info("GET ALL artworks ({})", artworks.size());
        return ResponseEntity.ok().body(artworks);
    }

    @Operation(summary = "Retrieve random artworks", description = "Fetches a specified number of random artworks.")
    @GetMapping(value = "/random", produces = "application/json")
    public ResponseEntity<List<Artwork>> getRandomArtworks(@RequestParam int numberOfArtworks) {
        List<Artwork> artworks = artworkServiceImpl.getRandomArtworks(numberOfArtworks);
        log.info("GET ALL random artworks ({})", artworks.size());
        return new ResponseEntity<>(artworks, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve artwork by ID", description = "Fetches details of a specific artwork using its unique ID.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Artwork> getArtwork(@PathVariable long id) {
        Artwork artwork = artworkServiceImpl.getArtworkById(id);
        log.info("GET artwork with id: {}", artwork.getId());
        return new ResponseEntity<>(artwork, HttpStatus.OK);
    }

    @Operation(summary = "Create a new artwork", description = "Creates a new artwork with the provided details and an associated image.")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Artwork> createArtwork(@Valid @RequestPart("artwork") Artwork artwork,
                                                 @RequestPart("file") MultipartFile imageFile) {
        Artwork createdEntity = artworkServiceImpl.createArtwork(artwork, imageFile);
        log.info("CREATE artwork with id: {}", createdEntity.getId());
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @Operation(summary = "Update artwork details", description = "Updates the details of an existing artwork without modifying its associated image.")
    @PutMapping(value = "update", produces = "application/json")
    public ResponseEntity<Artwork> updateArtwork(@Valid @RequestBody Artwork newArtwork) {
        Artwork updatedArtwork = artworkServiceImpl.updateArtwork(newArtwork);
        log.info("UPDATE artwork with id: {}", updatedArtwork.getId());
        return new ResponseEntity<>(updatedArtwork, HttpStatus.OK);
    }

    @Operation(summary = "Update artwork with image", description = "Updates the details of an existing artwork and replaces its associated image.")
    @PutMapping(value = "updateWithImage", produces = "application/json")
    public ResponseEntity<Artwork> updateArtwork(@Valid @RequestPart("artwork") Artwork newArtwork,
                                                 @RequestPart("file") MultipartFile imageFile) {
        Artwork updatedArtwork = artworkServiceImpl.updateArtwork(newArtwork, imageFile);
        log.info("UPDATE artwork with image with id: {}", updatedArtwork.getId());
        return new ResponseEntity<>(updatedArtwork, HttpStatus.OK);
    }

    @Operation(summary = "Delete artwork by ID", description = "Deletes an existing artwork using its unique ID.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable long id) {
        artworkServiceImpl.deleteArtwork(id);
        log.info("DELETE artwork with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
