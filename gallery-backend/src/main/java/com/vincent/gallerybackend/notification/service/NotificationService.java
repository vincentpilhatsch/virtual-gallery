package com.vincent.gallerybackend.notification.service;

import com.vincent.gallerybackend.notification.model.Notification;
import com.vincent.gallerybackend.notification.model.NotificationEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {

    List<Notification> getNotificationsFromUserByUserEmail(String email);

    NotificationEntity createNotification(Notification notification);

    NotificationEntity setNotificationAsRead(long id);

}
