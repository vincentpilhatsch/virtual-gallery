package com.vincent.gallerybackend.user.config;

import com.vincent.gallerybackend.user.model.Language;
import com.vincent.gallerybackend.user.model.Role;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Profile("!test")
@Configuration
public class UserConfig {

    private final PasswordEncoder passwordEncoder;

    // Inject PasswordEncoder via constructor
    public UserConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner userCommandLineRunner(UserRepository userRepository) {
        return args -> {
            UserEntity thomas = new UserEntity(
                    UUID.randomUUID(),
                    "thomas",
                    "müller",
                    "admin@bht.de",
                    passwordEncoder.encode("admin"),
                    "berlin",
                    "123456789",
                    Language.DE,
                    Role.ARTIST,
                    Set.of(1L, 2L),      // favoriteArtworkIds
                    Set.of(1L),          // acceptedEventIds
                    Set.of(1L, 2L),       // notificationIds
                    Set.of()      // requestedArtworkIds
            );

            UserEntity max = new UserEntity(
                    UUID.randomUUID(),
                    "max",
                    "steiner",
                    "steiner@bht.de",
                    passwordEncoder.encode("admin"),
                    "berlin",
                    "123456789",
                    Language.EN,
                    Role.USER,
                    Set.of(1L, 2L),          // favoriteArtworkIds
                    Set.of(1L),          // acceptedEventIds
                    Set.of(1L),       // notificationIds
                    Set.of()          // requestedArtworkIds
            );
            userRepository.saveAll(List.of(thomas, max));
        };
    }

}
