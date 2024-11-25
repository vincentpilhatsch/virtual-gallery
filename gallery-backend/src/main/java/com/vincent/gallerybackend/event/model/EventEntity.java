package com.vincent.gallerybackend.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "gallery_event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty()
    private String title;

    private String description;

    private String location;

    private LocalDateTime dateTime;

    @Lob
    private byte[] image;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "event_confirmed_users", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "user_id")
    private Set<UUID> confirmedUserIds;

}
