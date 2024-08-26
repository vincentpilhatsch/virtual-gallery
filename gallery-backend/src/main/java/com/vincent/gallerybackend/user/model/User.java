package com.vincent.gallerybackend.user.model;

import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String city;
    private String phone;
    private Language language;

//    private String visitIds;


}
