package com.vincent.gallerybackend.user.model;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String city;
    private String phone;
    private Language language;
    private Role role;
    private Set<Long> favoriteArtworkIds;
    private Set<Long> requestedArtworkIds;
    private Set<Long> acceptedEventIds;
    private Set<Long> notificationIds;

}
