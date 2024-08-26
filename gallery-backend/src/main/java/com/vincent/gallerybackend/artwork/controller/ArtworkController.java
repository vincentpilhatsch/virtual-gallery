package com.vincent.gallerybackend.artwork.controller;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.service.ArtworkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/artwork")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkServiceImpl userServiceImpl;

    @GetMapping(value = "/{id}", produces = "application/json")
    public Artwork getArtworkById(@PathVariable long id) {
        return userServiceImpl.getArtworkById(id);
    }

    @PostMapping
    public ResponseEntity<ArtworkEntity> createArtwork(@RequestBody Artwork artwork) {
        ArtworkEntity userEntity = userServiceImpl.createArtwork(artwork);
        return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ArtworkEntity> updateArtwork(@RequestBody Artwork newArtwork) {
        ArtworkEntity updatedArtworkEntity = userServiceImpl.updateArtwork(newArtwork);
        return new ResponseEntity<>(updatedArtworkEntity, HttpStatus.OK);
    }

}
