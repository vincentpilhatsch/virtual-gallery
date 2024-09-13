package com.vincent.gallerybackend.event.config;

import com.vincent.gallerybackend.event.model.EventEntity;
import com.vincent.gallerybackend.event.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Profile("!test")
@Configuration
public class EventConfig {

    @Bean
    CommandLineRunner eventCommandLineRunner(EventRepository eventRepository) {
        return args -> {
            EventEntity eventEntity = new EventEntity(
                    1L,
                    "Vernissage",
                    "Vernissage bei denen neue Kustwerke vorgestellt werden",
                    LocalDateTime.now(),
                    new byte[]{'1', '2', '3'},
                    Set.of(UUID.randomUUID(), UUID.randomUUID())
            );
            eventRepository.saveAll(List.of(eventEntity));
        };
    }

}
