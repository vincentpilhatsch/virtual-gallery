package com.vincent.gallerybackend.artwork.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Size(min = 2, max = 120, message = "Title length is invalid")
    @NotBlank
    private String title;

    private int width;

    private int height;

    @Size(min = 4, max = 4, message = "Invalid date format")
    @NotBlank
    private String yearOfCreation; // TODO: Change to int!! -> auch NotEmpty, da int

    @NotNull
    private LocalDateTime uploadedAt;

    @Size(min = 2, max = 120, message = "Technic length is invalid")
    private String technic;

    @NotNull
    private BigDecimal price;

    private boolean sold = Boolean.FALSE;

    @Lob
    @NotEmpty
    private byte[] image;

}
