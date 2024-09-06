package com.vincent.gallerybackend.event.config;

import com.vincent.gallerybackend.event.model.ArtistEntity;
import com.vincent.gallerybackend.event.model.Language;
import com.vincent.gallerybackend.event.repository.ArtistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.UUID;

@Profile("!test")
@Configuration
public class EventConfig {

    @Bean
    CommandLineRunner eventCommandLineRunner(EventRepository eventRepository) {
        return args -> {
            eventRepository.saveAll(List.of(eventEntity));
        };
    }

}
