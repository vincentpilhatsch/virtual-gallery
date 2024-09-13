package com.vincent.gallerybackend.artist.controller;

import com.vincent.gallerybackend.artist.service.ArtistServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistServiceImpl artistServiceImpl;

    @GetMapping(value = "/{email}", produces = "application/json")
    public boolean checkArtistExists(@PathVariable String email) {
        log.info("GET check if artist exist with email: {}", email);
        return artistServiceImpl.checkArtistExists(email);
    }

}
