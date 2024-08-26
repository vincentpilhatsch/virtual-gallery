package com.vincent.gallerybackend.artist.repository;

import com.vincent.gallerybackend.artist.model.ArtistEntity;
import com.vincent.gallerybackend.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, String> {}
