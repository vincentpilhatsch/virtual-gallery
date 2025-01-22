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
        for (int i = 1; i < 9; i++) {
            String path = "classpath:" + i + ".jpg";
            byte[] base64 = readFileToByteArray(path);
            images.add(base64);
        }

        return args -> {
            EventEntity eventEntity1 = new EventEntity(
                    1L,
                    "Vernissage",
                    "Erleben Sie die feierliche Vorstellung neuer Kunstwerke von Olivier Kéros. Tauschen Sie sich mit dem Künstler persönlich aus und entdecken Sie seine neuesten Werke in exklusivem Ambiente.",
                    "Hohenzollerndamm 216, 10781 Berlin",
                    LocalDateTime.now().plusWeeks(5),
                    images.get(5),
                    Set.of(UUID.randomUUID(), UUID.randomUUID())
            );

            EventEntity eventEntity2 = new EventEntity(
                    2L,
                    "Künstlergespräch",
                    "Ein inspirierender Abend mit Olivier Kéros. Erfahren Sie mehr über seine Arbeitsweise, seine Philosophie und die Geschichten hinter seinen Werken. Eine seltene Gelegenheit, die Kunst durch die Augen des Künstlers zu sehen.",
                    "Berliner Straße 16, 11721 Berlin",
                    LocalDateTime.now().minusDays(1),
                    images.get(6),
                    Set.of(UUID.randomUUID(), UUID.randomUUID())
            );

            EventEntity eventEntity3 = new EventEntity(
                    3L,
                    "After-Show-Party",
                    "Feiern Sie mit uns das Ende einer Ausstellung. Genießen Sie Musik, Drinks und gute Gespräche inmitten einer inspirierenden Kunstatmosphäre. Bringen Sie gerne Freunde und Verwandte mit",
                    "Eisenacher Straße 22, 10781 Berlin",
                    LocalDateTime.now().minusDays(1),
                    images.get(4),
                    Set.of(UUID.randomUUID(), UUID.randomUUID())
            );

            EventEntity eventEntity4 = new EventEntity(
                    4L,
                    "Neueröffnung",
                    "Feiern Sie die Neueröffnung unseres Galerieraums mit einer besonderen Auswahl von Werken von Olivier Kéros. Lassen Sie sich von der neuen Atmosphäre inspirieren und genießen Sie einen unvergesslichen Abend.",
                    "Nollendorf Straße 32, 10623 Berlin",
                    LocalDateTime.now().minusDays(1),
                    images.get(7),
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
