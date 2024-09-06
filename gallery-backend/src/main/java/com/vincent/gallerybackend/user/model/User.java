package com.vincent.gallerybackend.user.model;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String city;
    private String phone;
    private Language language;

//    private String visitIds;


}
