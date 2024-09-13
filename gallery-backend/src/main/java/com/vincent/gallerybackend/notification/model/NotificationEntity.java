package com.vincent.gallerybackend.notification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gallery_notification")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty()
    private String title;

    @NotEmpty()
    private String description;

    private LocalDateTime dateTime;

    private boolean read = Boolean.FALSE;

}
