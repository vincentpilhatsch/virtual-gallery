package com.vincent.gallerybackend.user.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginRequest implements AuthRequest {

    private String email;
    private String password;

}
