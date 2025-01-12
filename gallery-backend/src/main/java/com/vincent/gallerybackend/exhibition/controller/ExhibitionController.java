package com.vincent.gallerybackend.exhibition.controller;

import com.vincent.gallerybackend.exhibition.model.Exhibition;
import com.vincent.gallerybackend.exhibition.service.ExhibitionServiceImpl;
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
@RequestMapping(path = "/exhibition")
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionServiceImpl exhibitionServiceImpl;

    @Operation(summary = "Retrieve all exhibitions", description = "Fetches a list of all exhibitions available in the system.")
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Exhibition>> getExhibitions() {
        List<Exhibition> exhibitions = exhibitionServiceImpl.getExhibitions();
        log.info("GET ALL exhibitions ({})", exhibitions.size());
        return new ResponseEntity<>(exhibitions, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve the latest exhibition", description = "Fetches the most recent exhibition based on the creation or start date.")
    @GetMapping(value = "/latest", produces = "application/json")
    public ResponseEntity<Exhibition> getLatestExhibition() {
        Exhibition exhibition = exhibitionServiceImpl.getLatestExhibition();
        log.info("GET LATEST exhibition ({})", exhibition.getTitle());
        return new ResponseEntity<>(exhibition, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve exhibition by ID", description = "Fetches details of a specific exhibition using its unique ID.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Exhibition> getExhibition(@PathVariable long id) {
        Exhibition exhibition = exhibitionServiceImpl.getExhibitionById(id);
        log.info("GET exhibition: {}", exhibition.getTitle());
        return new ResponseEntity<>(exhibition, HttpStatus.OK);
    }

    @Operation(summary = "Create a new exhibition", description = "Creates a new exhibition with the provided details and an associated image.")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Exhibition> createExhibition(@Valid @RequestPart("exhibition") Exhibition exhibition,
                                                       @RequestPart("file") MultipartFile imageFile) {
        Exhibition createdExhibition = exhibitionServiceImpl.createExhibition(exhibition, imageFile);
        log.info("CREATE exhibition: {}", exhibition.getTitle());
        return new ResponseEntity<>(createdExhibition, HttpStatus.CREATED);
    }

    @Operation(summary = "Update exhibition details", description = "Updates the details of an existing exhibition without modifying its associated image.")
    @PutMapping(value = "update", produces = "application/json")
    public ResponseEntity<Exhibition> updateExhibition(@Valid @RequestBody Exhibition newExhibition) {
        Exhibition exhibition = exhibitionServiceImpl.updateExhibition(newExhibition);
        log.info("UPDATE exhibition: {}", newExhibition.getTitle());
        return new ResponseEntity<>(exhibition, HttpStatus.OK);
    }

    @Operation(summary = "Update exhibition with image", description = "Updates the details of an existing exhibition and replaces its associated image.")
    @PutMapping(value = "updateWithImage", produces = "application/json")
    public ResponseEntity<Exhibition> updateExhibition(@Valid @RequestPart("exhibition") Exhibition newExhibition,
                                                       @RequestPart("file") MultipartFile imageFile) {
        Exhibition exhibition = exhibitionServiceImpl.updateExhibition(newExhibition, imageFile);
        log.info("UPDATE exhibition with image: {}", newExhibition.getTitle());
        return new ResponseEntity<>(exhibition, HttpStatus.OK);
    }

    @Operation(summary = "Delete exhibition by ID", description = "Deletes an existing exhibition using its unique ID.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteExhibition(@PathVariable long id) {
        exhibitionServiceImpl.deleteExhibition(id);
        log.info("DELETE exhibition with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
