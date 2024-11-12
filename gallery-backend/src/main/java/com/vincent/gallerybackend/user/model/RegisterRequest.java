package com.vincent.gallerybackend.user.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegisterRequest implements AuthRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
