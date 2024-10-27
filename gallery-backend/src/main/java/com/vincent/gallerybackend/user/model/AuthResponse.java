package com.vincent.gallerybackend.user.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthResponse {
    private User user;
    private String jwt;
}
