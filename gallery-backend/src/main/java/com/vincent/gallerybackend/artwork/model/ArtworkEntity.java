package com.vincent.gallerybackend.artwork.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gallery_artwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArtworkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String title;

    private int width;

    private int height;

    @NotEmpty
    @Size(min = 4, max = 4)
    private String yearOfCreation;

    @NotNull
    private LocalDateTime uploadedAt;

    private String technic;

    @NotNull
    private BigDecimal price;

    private boolean sold = Boolean.FALSE;

    @Lob
    @NotEmpty
    private byte[] image;

}
