package com.vincent.gallerybackend.exhibition.controller;

import com.vincent.gallerybackend.exhibition.model.Exhibition;
import com.vincent.gallerybackend.exhibition.service.ExhibitionServiceImpl;
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

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Exhibition>> getExhibitions() {
        List<Exhibition> exhibitions = exhibitionServiceImpl.getExhibitions();
        log.info("GET ALL exhibitions ({})", exhibitions.size());
        return new ResponseEntity<>(exhibitions, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Exhibition> getExhibition(@PathVariable long id) {
        Exhibition exhibition = exhibitionServiceImpl.getExhibitionById(id);
        log.info("GET exhibition: {}", exhibition.getTitle());
        return new ResponseEntity<>(exhibition, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Exhibition> createExhibition(@Valid @RequestPart("exhibition") Exhibition exhibition,
                                                       @RequestPart("file") MultipartFile imageFile) {
        Exhibition createdExhibition = exhibitionServiceImpl.createExhibition(exhibition, imageFile);
        log.info("CREATE exhibition: {}", exhibition.getTitle());
        return new ResponseEntity<>(createdExhibition, HttpStatus.CREATED);
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<Exhibition> updateExhibition(@Valid @RequestPart("exhibition") Exhibition newExhibition,
                                                       @RequestPart("file") MultipartFile imageFile) {
        Exhibition exhibition = exhibitionServiceImpl.updateExhibition(newExhibition, imageFile);
        log.info("UPDATE exhibition: {}", newExhibition.getTitle());
        return new ResponseEntity<>(exhibition, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteExhibition(@PathVariable long id) {
        exhibitionServiceImpl.deleteExhibition(id);
        log.info("DELETE exhibition with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
