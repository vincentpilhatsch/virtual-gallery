package com.vincent.gallerybackend.artwork.config;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Profile("!test")
@Configuration
public class ArtworkConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    CommandLineRunner artworkCommandLineRunner(ArtworkRepository artworkRepository) throws IOException {
        List<byte[]> images = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            String path = "classpath:" + i + ".jpg";
            byte[] base64 = readFileToByteArray(path);
            images.add(base64);
        }

        return args -> {
            List<ArtworkEntity> artworks = List.of(
                    new ArtworkEntity(1L, "Dripping Water", 120, 160, "2021", LocalDateTime.now(), "Acryl", new BigDecimal("4450"), false, images.get(0)),
                    new ArtworkEntity(2L, "Burning Fire", 120, 160, "2021", LocalDateTime.now(), "Collage", new BigDecimal("3250"), false, images.get(1)),
                    new ArtworkEntity(3L, "Misty Mountains", 100, 140, "2022", LocalDateTime.now(), "Acryl", new BigDecimal("3520"), true, images.get(2)),
                    new ArtworkEntity(4L, "Silent Forest", 130, 170, "2022", LocalDateTime.now(), "Aquarell", new BigDecimal("4600"), false, images.get(3)),
                    new ArtworkEntity(5L, "Starry Night", 140, 180, "2023", LocalDateTime.now(), "Collage", new BigDecimal("5500"), false, images.get(4)),
                    new ArtworkEntity(6L, "Ocean Waves", 110, 150, "2023", LocalDateTime.now(), "Acryl", new BigDecimal("4750"), false, images.get(5)),
                    new ArtworkEntity(7L, "Desert Dunes", 120, 160, "2021", LocalDateTime.now(), "Aquarell", new BigDecimal("3350"), false, images.get(6)),
                    new ArtworkEntity(8L, "Urban Jungle", 125, 165, "2022", LocalDateTime.now(), "Acryl", new BigDecimal("3450"), false, images.get(7)),
                    new ArtworkEntity(9L, "Golden Sunset", 135, 175, "2022", LocalDateTime.now(), "Aquarell", new BigDecimal("4820"), true, images.get(8)));
            artworkRepository.saveAll(artworks);
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
