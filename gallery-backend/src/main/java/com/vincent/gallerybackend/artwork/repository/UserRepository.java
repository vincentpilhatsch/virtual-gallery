package com.vincent.gallerybackend.artwork.repository;

import com.vincent.gallerybackend.artwork.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {}
