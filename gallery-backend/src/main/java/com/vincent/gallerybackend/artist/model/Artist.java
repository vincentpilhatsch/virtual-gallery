package com.vincent.gallerybackend.artist.model;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class Artist {

    private UUID id;
    private String email;
    private String password;
    private Language language;

//    private String visitIds;


}
