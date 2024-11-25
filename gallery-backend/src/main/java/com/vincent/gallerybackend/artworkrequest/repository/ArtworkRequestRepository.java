package com.vincent.gallerybackend.artworkrequest.repository;

import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRequestRepository extends JpaRepository<ArtworkRequestEntity, Long> {
}
