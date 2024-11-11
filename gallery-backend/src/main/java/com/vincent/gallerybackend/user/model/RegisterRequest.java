package com.vincent.gallerybackend.user.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
