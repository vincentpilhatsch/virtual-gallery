package com.vincent.gallerybackend.event.repository;

import com.vincent.gallerybackend.event.model.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {

    Optional<ArtistEntity> findByEmail(String email);

}
