package com.vincent.gallerybackend.artist.model;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class Artist {

    private String id;
    private String email;
    private String password;
    private Language language;

//    private String visitIds;


}
