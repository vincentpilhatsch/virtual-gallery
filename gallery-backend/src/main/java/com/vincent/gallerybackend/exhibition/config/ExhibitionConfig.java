package com.vincent.gallerybackend.exhibition.config;

import com.vincent.gallerybackend.exhibition.model.ExhibitionEntity;
import com.vincent.gallerybackend.exhibition.repository.ExhibitionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("!test")
@Configuration
public class ExhibitionConfig {

    private final ResourceLoader resourceLoader;

    public ExhibitionConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    CommandLineRunner exhibitionCommandLineRunner(ExhibitionRepository exhibitionRepository) throws IOException {
        List<byte[]> images = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            String path = "classpath:" + i + ".jpg";
            byte[] base64 = readFileToByteArray(path);
            images.add(base64);
        }

        return args -> {
            ExhibitionEntity exhibitionEntity1 = new ExhibitionEntity(
                    1L,
                    "Shades of Transformation",
                    "Diese Ausstellung lädt Sie ein, die transformative Kraft der Farben zu entdecken. Olivier Kéros zeigt eine Auswahl seiner neuesten Werke, die das Spiel zwischen Farbe und Kontrast in den Mittelpunkt rücken.",
                    "Hohenzollerndamm 216, 10781 Berlin",
                    LocalDate.now().plusDays(4),
                    LocalDate.now().plusDays(50),
                    images.get(2)
            );

            ExhibitionEntity exhibitionEntity2 = new ExhibitionEntity(
                    2L,
                    "Emotions on Canvas",
                    "Eine Reise durch die Tiefen menschlicher Gefühle. Diese Ausstellung präsentiert kraftvolle Werke, die durch abstrakte Formen und lebendige Farben eine intensive Wirkung entfalten.",
                    "Berliner Straße 16, 11721 Berlin",
                    LocalDate.now().minusDays(7),
                    LocalDate.now().minusDays(2),
                    images.get(3)
            );

            ExhibitionEntity exhibitionEntity3 = new ExhibitionEntity(
                    3L,
                    "The Chaos of Silence",
                    "In dieser einzigartigen Ausstellung verbindet Olivier Kéros das scheinbare Chaos abstrakter Formen mit der Ruhe der Stille. Ein Erlebnis für die Sinne und den Geist.",
                    "Eisenacher Straße 16, 11721 Berlin",
                    LocalDate.now().minusDays(10),
                    LocalDate.now().minusDays(5),
                    images.get(4)
            );
            exhibitionRepository.saveAll(List.of(
                    exhibitionEntity1, exhibitionEntity2, exhibitionEntity3)
            );
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
