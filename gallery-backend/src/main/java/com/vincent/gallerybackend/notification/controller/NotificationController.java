package com.vincent.gallerybackend.notification.controller;

import com.vincent.gallerybackend.notification.model.Notification;
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
    public ResponseEntity<List<Notification>> getNotificationsFromUser(@PathVariable String email) {
        List<Notification> notifications = notificationServiceImpl.getNotificationsFromUserByUserEmail(email);
        log.info("GET notifications ({}): {}", notifications.size(), notifications);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Notification> createNotification(@Valid @RequestBody Notification notification) {
        Notification createdNotification = notificationServiceImpl.createNotification(notification);
        log.info("CREATE notification: {}", notification);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Notification> setNotificationAsRead(@PathVariable long id) {
        Notification notification = notificationServiceImpl.setNotificationAsRead(id);
        log.info("UPDATE notification: {}", notification);
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

}
