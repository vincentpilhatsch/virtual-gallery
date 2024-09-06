package com.vincent.gallerybackend.artist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "gallery_artist")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty()
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotEmpty()
    private String password;

    @NotNull
    private Language language;

//    private String visitIds;


}
