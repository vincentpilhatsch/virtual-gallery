package com.vincent.gallerybackend.artist.controller;

import com.vincent.gallerybackend.artist.service.ArtistServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistServiceImpl artistServiceImpl;

    @GetMapping(value = "/{email}", produces = "application/json")
    public boolean checkArtistExists(@PathVariable String email) {
        return artistServiceImpl.checkArtistExists(email);
    }

}
