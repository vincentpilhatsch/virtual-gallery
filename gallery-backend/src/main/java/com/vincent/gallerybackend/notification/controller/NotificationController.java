package com.vincent.gallerybackend.notification.controller;

import com.vincent.gallerybackend.notification.model.Notification;
import com.vincent.gallerybackend.notification.model.NotificationEntity;
import com.vincent.gallerybackend.notification.service.NotificationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationServiceImpl;

    @GetMapping(value = "/{email}", produces = "application/json")
    public List<Notification> getNotificationsFromUser(@PathVariable String email) {
        List<Notification> notifications = notificationServiceImpl.getNotificationsFromUserByUserEmail(email);
        log.info("GET notifications ({}): {}", notifications.size(), notifications);
        return notifications;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<NotificationEntity> createNotification(@Valid @RequestBody Notification notification) {
        NotificationEntity notificationEntity = notificationServiceImpl.createNotification(notification);
        log.info("CREATE notification: {}", notification);
        return new ResponseEntity<>(notificationEntity, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<NotificationEntity> setNotificationAsRead(@PathVariable long id) {
        NotificationEntity notificationEntity = notificationServiceImpl.setNotificationAsRead(id);
        log.info("UPDATE notification: {}", notificationEntity);
        return new ResponseEntity<>(notificationEntity, HttpStatus.OK);
    }

}
