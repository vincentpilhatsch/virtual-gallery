package com.vincent.gallerybackend.notification.service;

import com.vincent.gallerybackend.notification.model.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {

    List<Notification> getNotificationsFromUserByUserEmail(String email);

    Notification createNotification(Notification notification);

    Notification setNotificationAsRead(long id);

}
