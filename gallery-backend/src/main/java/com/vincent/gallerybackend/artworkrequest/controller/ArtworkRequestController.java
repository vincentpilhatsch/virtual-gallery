package com.vincent.gallerybackend.artworkrequest.controller;

import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.service.ArtworkRequestServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/artwork-request")
@RequiredArgsConstructor
public class ArtworkRequestController {

    private final ArtworkRequestServiceImpl artworkRequestServiceImpl;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<ArtworkRequest>> getArtworkRequests() {
        List<ArtworkRequest> artworkRequests = artworkRequestServiceImpl.getArtworkRequests();
        log.info("GET ALL artworkRequests ({})", artworkRequests.size());
        return new ResponseEntity<>(artworkRequests, HttpStatus.OK);

    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ArtworkRequest> getArtworkRequest(@PathVariable long id) {
        ArtworkRequest artworkRequest = artworkRequestServiceImpl.getArtworkRequestById(id);
        log.info("GET artworkRequest with id: {}", artworkRequest.getId());
        return new ResponseEntity<>(artworkRequest, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ArtworkRequest> createArtworkRequest(
            @Valid @RequestPart("artworkRequest") ArtworkRequest artworkRequest
    ) {
        ArtworkRequest createdEntity = artworkRequestServiceImpl.createArtworkRequest(artworkRequest);
        log.info("CREATE artworkRequest with id: {}", artworkRequest.getId());
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteArtworkRequest(@PathVariable long id) {
        artworkRequestServiceImpl.deleteArtworkRequest(id);
        log.info("DELETE artworkRequest with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
