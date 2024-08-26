package com.vincent.gallerybackend.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "gallery_user") // "user" is reserved
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class UserEntity {

    @Id
    private String id;

    @NotEmpty()
    @Size(min = 2, max = 30, message = "Firstname is to small/long")
    private String firstName;

    @NotEmpty()
    @Size(min = 2, max = 50, message = "Lastname is to small/long")
    private String lastName;

    @NotEmpty()
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty()
    private String password;

    private String city;

    private String phone;

    @NotNull
    private Language language;

//    private String favoriteArtworkIds;
//    private String acceptedEventIds;
//    private String galleryVisitIds;


}
