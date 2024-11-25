package com.vincent.gallerybackend.artwork;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ArtworkRequest {

    private long id;
    private String description;
    private LocalDateTime requestTime;
    private UUID userId;
    private long artworkId;

}
