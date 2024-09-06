package com.vincent.gallerybackend.event.service;

import org.springframework.stereotype.Service;

@Service
public interface ArtistService {

    boolean checkArtistExists(String email);

}
