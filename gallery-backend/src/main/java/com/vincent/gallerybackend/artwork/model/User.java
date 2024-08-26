package com.vincent.gallerybackend.artwork.model;

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
