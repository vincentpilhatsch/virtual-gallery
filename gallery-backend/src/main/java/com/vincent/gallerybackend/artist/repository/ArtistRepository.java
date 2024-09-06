package com.vincent.gallerybackend.artist.repository;

import com.vincent.gallerybackend.artist.model.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {

    Optional<ArtistEntity> findByEmail(String email);

}
