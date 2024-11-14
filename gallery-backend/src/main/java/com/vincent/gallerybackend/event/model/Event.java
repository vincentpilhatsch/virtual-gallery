package com.vincent.gallerybackend.event.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Event {

    private long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime dateTime;
    private byte[] image;
    private Set<UUID> confirmedUserIds;

}
