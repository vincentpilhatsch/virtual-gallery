package com.vincent.gallerybackend.notification.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class Notification {

    private long id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private boolean read;

}
