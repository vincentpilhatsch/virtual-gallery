package com.vincent.gallerybackend.artwork.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.service.ArtworkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/artwork")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkServiceImpl artworkServiceImpl;

    @GetMapping(value = "/all")
    public List<Artwork> getArtworks() {
        return artworkServiceImpl.getArtworks();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Artwork getArtworkById(@PathVariable long id) {
        return artworkServiceImpl.getArtworkById(id);
    }

    @PostMapping
    public ResponseEntity<ArtworkEntity> createArtwork(@RequestPart("artwork") Artwork artwork, // RequestParam?
                                                       @RequestPart("file") MultipartFile imageFile) {
        ArtworkEntity artworkEntity = artworkServiceImpl.createArtwork(artwork, imageFile);
        return new ResponseEntity<>(artworkEntity, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ArtworkEntity> updateArtwork(@RequestPart("artwork") Artwork newArtwork,
                                                       @RequestPart("file") MultipartFile imageFile) {
        ArtworkEntity updatedArtworkEntity = artworkServiceImpl.updateArtwork(newArtwork, imageFile);
        return new ResponseEntity<>(updatedArtworkEntity, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteArtwork(@PathVariable long id) {
        artworkServiceImpl.deleteArtwork(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
