package com.vincent.gallerybackend.artworkrequest.controller;

import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.model.CompleteArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.service.ArtworkRequestServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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

    @Operation(summary = "Retrieve all artwork requests", description = "Fetches a list of all artwork requests available in the system.")
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<ArtworkRequest>> getArtworkRequests() {
        List<ArtworkRequest> artworkRequests = artworkRequestServiceImpl.getArtworkRequests();
        log.info("GET ALL artworkRequests ({})", artworkRequests.size());
        return new ResponseEntity<>(artworkRequests, HttpStatus.OK);

    }

    @Operation(summary = "Retrieve all complete artwork requests", description = "Fetches a list of all complete artwork requests available in the system.")
    @GetMapping(value = "/allComplete", produces = "application/json")
    public ResponseEntity<List<CompleteArtworkRequest>> getCompleteArtworkRequests() {
        List<CompleteArtworkRequest> completeArtworkRequest = artworkRequestServiceImpl.getCompleteArtworkRequests();
        log.info("GET ALL COMPLETE artworkRequests ({})", completeArtworkRequest.size());
        return new ResponseEntity<>(completeArtworkRequest, HttpStatus.OK);

    }

    @Operation(summary = "Retrieve artwork request by ID", description = "Fetches details of a specific artwork request using its unique ID.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ArtworkRequest> getArtworkRequest(@PathVariable long id) {
        ArtworkRequest artworkRequest = artworkRequestServiceImpl.getArtworkRequestById(id);
        log.info("GET artworkRequest with id: {}", artworkRequest.getId());
        return new ResponseEntity<>(artworkRequest, HttpStatus.OK);
    }

    @Operation(summary = "Create a new artwork request", description = "Creates a new artwork request with the provided details.")
    @PostMapping(produces = "application/json")
    public ResponseEntity<ArtworkRequest> createArtworkRequest(@Valid @RequestBody ArtworkRequest artworkRequest) {
        ArtworkRequest createdEntity = artworkRequestServiceImpl.createArtworkRequest(artworkRequest);
        log.info("CREATE artworkRequest with id: {}", artworkRequest.getId());
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @Operation(summary = "Accept a artwork request", description = "Accept artwork requests for the artwork.")
    @CacheEvict(value = "artworks", allEntries = true)
    @PostMapping(value = "accept", produces = "application/json")
    public ResponseEntity<ArtworkRequest> acceptArtworkRequest(@Valid @RequestBody ArtworkRequest artworkRequest) {
        artworkRequestServiceImpl.acceptArtworkRequest(artworkRequest);
        log.info("ACCEPTED ALL artworkRequest for artwork: {}", artworkRequest.getArtworkId());
        return new ResponseEntity<>(artworkRequest, HttpStatus.OK);
    }

    @Operation(summary = "Reject a artwork request", description = "Reject artwork request for the user and artist.")
    @PostMapping(value = "reject", produces = "application/json")
    public ResponseEntity<ArtworkRequest> rejectArtworkRequest(@Valid @RequestBody ArtworkRequest artworkRequest) {
        artworkRequestServiceImpl.rejectArtworkRequest(artworkRequest);
        log.info("REJECT artworkRequest with id: {}", artworkRequest.getArtworkId());
        return new ResponseEntity<>(artworkRequest, HttpStatus.OK);
    }

    @Operation(summary = "Delete artwork request by ID", description = "Deletes an existing artwork request using its unique ID.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteArtworkRequest(@PathVariable long id) {
        artworkRequestServiceImpl.deleteArtworkRequest(id);
        log.info("DELETE artworkRequest with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
