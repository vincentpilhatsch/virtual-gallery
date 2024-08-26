package com.vincent.gallerybackend.artwork.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class Artwork {

    private long id;
    private String title;
    private int width;
    private int height;
    private String yearOfCreation;
    private LocalDateTime uploadedAt;
    private String technic;
    private BigDecimal price;
    private boolean sold;
    private byte[] image;

}
