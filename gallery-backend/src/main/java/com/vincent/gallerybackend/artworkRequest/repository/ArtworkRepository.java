package com.vincent.gallerybackend.artwork;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<ArtworkEntity, Long> {
}
