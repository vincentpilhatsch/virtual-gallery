package com.vincent.gallerybackend.artist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "gallery_artist")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class ArtistEntity {

    @Id
    private String id;

    @NotEmpty()
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty()
    private String password;

    @NotNull
    private Language language;

//    private String visitIds;


}
