package com.vincent.gallerybackend.artist.service;

import org.springframework.stereotype.Service;

@Service
public interface ArtistService {

    boolean checkArtistExists(String email);

}
