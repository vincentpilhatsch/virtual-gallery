package com.vincent.gallerybackend.notification.repository;

import com.vincent.gallerybackend.notification.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

}
