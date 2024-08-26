package com.vincent.gallerybackend.artwork.config;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class ArtworkConfig {

    @Bean
    CommandLineRunner artworkCommandLineRunner(ArtworkRepository artworkRepository) {
        return args -> {
            ArtworkEntity drippingWater = new ArtworkEntity(0L,
                    "Dripping Water",
                    120,
                    160,
                    "2021",
                    LocalDateTime.now(),
                    "Gemalt mit Pinsel und Sprühdose",
                    new BigDecimal("123.45"),
                    false,
                    new byte[]{'1', '2', '3'});
            ArtworkEntity burningFire = new ArtworkEntity(0L,
                    "Burning Fire",
                    120,
                    160,
                    "2021",
                    LocalDateTime.now(),
                    "Gemalt mit Pinsel und Sprühdose",
                    new BigDecimal("123.45"),
                    false,
                    new byte[]{'1', '2', '3'});
            artworkRepository.saveAll(List.of(drippingWater, burningFire));
        };
    }

}
