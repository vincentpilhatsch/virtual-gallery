package com.vincent.gallerybackend.artworkrequest.service;

import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestEntity;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestMapper;
import com.vincent.gallerybackend.artworkrequest.repository.ArtworkRequestRepository;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtworkRequestServiceImpl implements ArtworkRequestService {

    private final ArtworkRequestRepository artworkRequestRepository;
    private final ArtworkRequestMapper artworkRequestMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ArtworkRequest> getArtworkRequests() {
        List<ArtworkRequestEntity> artworkRequestEntities = artworkRequestRepository.findAll();
        return artworkRequestMapper.artworkRequestsDAOtoDTO(artworkRequestEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public ArtworkRequest getArtworkRequestById(long id) {
        ArtworkRequestEntity artworkRequestEntity = findById(id);
        return artworkRequestMapper.artworkRequestDAOtoDTO(artworkRequestEntity);
    }

    private ArtworkRequestEntity findById(long id) {
        return artworkRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ArtworkRequest not found"));
    }

    @Transactional
    @Override
    public ArtworkRequest createArtworkRequest(ArtworkRequest artworkRequest) {
        ArtworkRequestEntity artworkRequestEntity = artworkRequestMapper.artworkRequestDTOtoDAO(artworkRequest);
        artworkRequestEntity.setRequestTime(LocalDateTime.now());
        artworkRequestRepository.save(artworkRequestEntity);
        return artworkRequestMapper.artworkRequestDAOtoDTO(artworkRequestEntity);
    }

    @Transactional
    @Override
    public void deleteArtworkRequest(long id) {
        ArtworkRequestEntity artworkRequestEntity = findById(id);
        artworkRequestRepository.delete(artworkRequestEntity);
    }

}
