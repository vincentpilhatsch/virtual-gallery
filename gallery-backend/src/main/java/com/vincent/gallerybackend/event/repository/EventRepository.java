package com.vincent.gallerybackend.event.repository;

import com.vincent.gallerybackend.event.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

}
