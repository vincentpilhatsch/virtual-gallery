package com.vincent.gallerybackend.user.config;

import com.vincent.gallerybackend.user.model.Language;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;

@Profile("!test")
@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner userCommandLineRunner(UserRepository userRepository) {
        return args -> {
            UserEntity thomas = new UserEntity(UUID.randomUUID(),
                    "thomas",
                    "müller",
                    "müller@bht.de",
                    "müller123",
                    "berlin",
                    "123456789",
                    Language.DE);
            UserEntity max = new UserEntity(UUID.randomUUID(),
                    "max",
                    "steiner",
                    "steiner@bht.de",
                    "müller123",
                    "berlin",
                    "123456789",
                    Language.ENG);
            userRepository.saveAll(List.of(thomas, max));
        };
    }

}
