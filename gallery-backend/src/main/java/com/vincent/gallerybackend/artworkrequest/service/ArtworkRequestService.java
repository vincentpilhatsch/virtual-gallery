package com.vincent.gallerybackend.artworkrequest.service;

import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.model.CompleteArtworkRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtworkRequestService {

    ArtworkRequest getArtworkRequestById(long id);

    List<ArtworkRequest> getArtworkRequests();

    List<CompleteArtworkRequest> getCompleteArtworkRequests();

    ArtworkRequest createArtworkRequest(ArtworkRequest artworkRequest);

    void acceptArtworkRequest(ArtworkRequest artworkRequest);

    void rejectArtworkRequest(ArtworkRequest artworkRequest);

    void deleteArtworkRequest(long id);

}
