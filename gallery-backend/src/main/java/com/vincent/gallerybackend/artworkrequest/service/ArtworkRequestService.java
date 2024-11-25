package com.vincent.gallerybackend.artworkrequest.service;

import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtworkRequestService {

    ArtworkRequest getArtworkRequestById(long id);

    List<ArtworkRequest> getArtworkRequests();

    ArtworkRequest createArtworkRequest(ArtworkRequest artworkRequest);

    void deleteArtworkRequest(long id);

}
