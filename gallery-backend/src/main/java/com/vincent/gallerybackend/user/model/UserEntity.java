package com.vincent.gallerybackend.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "gallery_user") // "user" is reserved
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty()
    @Size(min = 2, max = 30, message = "Firstname is to small/long")
    private String firstName;

    @NotEmpty()
    @Size(min = 2, max = 50, message = "Lastname is to small/long")
    private String lastName;

    @NotEmpty()
    @Column(unique = true)
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty()
    private String password;

    private String city;

    private String phone;

    private Language language = Language.DE;

    private Role role = Role.USER;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_favorite_artworks", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "artwork_id")
    private Set<Long> favoriteArtworkIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_requested_artworks", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "artwork_id")
    private Set<Long> requestedArtworkIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_accepted_events", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "event_id")
    private Set<Long> acceptedEventIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_notifications", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "notification_id")
    private Set<Long> notificationIds;

}
