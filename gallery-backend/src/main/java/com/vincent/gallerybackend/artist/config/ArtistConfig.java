package com.vincent.gallerybackend.artist.config;

import com.vincent.gallerybackend.artist.model.ArtistEntity;
import com.vincent.gallerybackend.artist.model.Language;
import com.vincent.gallerybackend.artist.repository.ArtistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;

@Profile("!test")
@Configuration
public class ArtistConfig {

    @Bean
    CommandLineRunner artistCommandLineRunner(ArtistRepository artistRepository) {
        return args -> {
            ArtistEntity artistEntity = new ArtistEntity(UUID.randomUUID(),
                    "keros@bht.de",
                    "oliver123",
                    Language.DE);
            artistRepository.saveAll(List.of(artistEntity));
        };
    }

}
