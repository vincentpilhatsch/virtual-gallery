package com.vincent.gallerybackend.artist.controller;

import com.vincent.gallerybackend.artist.service.ArtistServiceImpl;
import com.vincent.gallerybackend.user.model.User;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistServiceImpl artistServiceImpl;

    @GetMapping(value = "/{id}", produces = "application/json")
    public boolean checkArtistExists(@PathVariable String id) {
        return artistServiceImpl.checkArtistExists(id);
    }

}
