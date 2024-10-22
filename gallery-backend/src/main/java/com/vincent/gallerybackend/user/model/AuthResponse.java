package com.vincent.gallerybackend.user.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthResponse {
    private String status;
    private String jwt;
}
