package com.vincent.gallerybackend.event.controller;

import com.vincent.gallerybackend.event.service.ArtistServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/artist")
@RequiredArgsConstructor
public class EventController {

    private final ArtistServiceImpl artistServiceImpl;

    @GetMapping(value = "/{email}", produces = "application/json")
    public boolean checkArtistExists(@PathVariable String email) {
        return artistServiceImpl.checkArtistExists(email);
    }

}
