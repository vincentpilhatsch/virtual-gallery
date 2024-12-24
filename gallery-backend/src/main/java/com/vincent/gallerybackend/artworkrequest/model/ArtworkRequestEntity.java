package com.vincent.gallerybackend.artworkrequest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "gallery_artwork_request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArtworkRequestEntity {
    // TODO: when artworkRequest got accepted then every request for the artwork needs to get deleted

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String description;

    @NotNull
    private LocalDateTime requestTime;

    private UUID userId;

    private long artworkId;

}
