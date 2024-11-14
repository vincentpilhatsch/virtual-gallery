package com.vincent.gallerybackend.event.config;

import com.vincent.gallerybackend.event.model.EventEntity;
import com.vincent.gallerybackend.event.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Profile("!test")
@Configuration
public class EventConfig {

    private final ResourceLoader resourceLoader;

    public EventConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    CommandLineRunner eventCommandLineRunner(EventRepository eventRepository) throws IOException {
        List<byte[]> images = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            String path = "classpath:" + i + ".jpg";
            byte[] base64 = readFileToByteArray(path);
            images.add(base64);
        }

        return args -> {
            EventEntity eventEntity1 = new EventEntity(
                    1L,
                    "Vernissage",
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo irmod tempor.",
                    "Hohenzollerndamm 216, 10781 Berlin",
                    LocalDateTime.now().plusDays(1),
                    images.get(1),
                    Set.of(UUID.randomUUID(), UUID.randomUUID())
            );
            EventEntity eventEntity2 = new EventEntity(
                    2L,
                    "Vorstellung",
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo irmod tempor.",
                    "Berliner Straße 16, 11721 Berlin",
                    LocalDateTime.now().minusDays(1),
                    images.get(2),
                    Set.of(UUID.randomUUID(), UUID.randomUUID())
            );
            EventEntity eventEntity3 = new EventEntity(
                    3L,
                    "Party",
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo irmod tempor.",
                    "Eisenacher Straße 16, 11721 Berlin",
                    LocalDateTime.now().minusDays(1),
                    images.get(3),
                    Set.of(UUID.randomUUID(), UUID.randomUUID())
            );
            EventEntity eventEntity4 = new EventEntity(
                    4L,
                    "Neueröffnung",
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo irmod tempor.",
                    "Eisenacher Straße 16, 11721 Berlin",
                    LocalDateTime.now().minusDays(1),
                    images.get(4),
                    Set.of(UUID.randomUUID(), UUID.randomUUID())
            );
            eventRepository.saveAll(List.of(eventEntity1, eventEntity2, eventEntity3, eventEntity4));
        };
    }

    public byte[] readFileToByteArray(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource(filePath);
        try (FileInputStream fis = new FileInputStream(resource.getFile())) {
            byte[] bytesArray = new byte[(int) resource.contentLength()];
            fis.read(bytesArray);
            return bytesArray;
        }
    }

}
