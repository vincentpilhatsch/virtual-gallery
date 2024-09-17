package com.vincent.gallerybackend.artwork.config;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Profile("!test")
@Configuration
public class ArtworkConfig {

    @Bean
    CommandLineRunner artworkCommandLineRunner(ArtworkRepository artworkRepository) throws IOException {
        List<byte[]> images = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            String path = "C:/Users/v-pil/Desktop/gallery-website/virtual-gallery/gallery-backend/src/main/resources/" + i + ".jpg";
            byte[] base64 = readFileToByteArray(path);
            images.add(base64);
        }

        return args -> {
            List<ArtworkEntity> artworks = List.of(
                    new ArtworkEntity(1L, "Dripping Water", 120, 160, "2021", LocalDateTime.now(), "Gemalt mit Pinsel und Sprühdose", new BigDecimal("145"), false, images.get(0)),
                    new ArtworkEntity(2L, "Burning Fire", 120, 160, "2021", LocalDateTime.now(), "Gemalt mit Pinsel und Sprühdose", new BigDecimal("120"), false, images.get(1)),
                    new ArtworkEntity(3L, "Misty Mountains", 100, 140, "2022", LocalDateTime.now(), "Aquarell auf Papier", new BigDecimal("150"), true, images.get(2)),
                    new ArtworkEntity(4L, "Silent Forest", 130, 170, "2022", LocalDateTime.now(), "Öl auf Leinwand", new BigDecimal("200.00"), false, images.get(3)),
                    new ArtworkEntity(5L, "Starry Night", 140, 180, "2023", LocalDateTime.now(), "Öl auf Leinwand", new BigDecimal("250.00"), true, images.get(4)),
                    new ArtworkEntity(6L, "Ocean Waves", 110, 150, "2023", LocalDateTime.now(), "Gemalt mit Sprühdose", new BigDecimal("175.00"), false, images.get(5)),
                    new ArtworkEntity(7L, "Desert Dunes", 120, 160, "2021", LocalDateTime.now(), "Aquarell auf Papier", new BigDecimal("130.00"), true, images.get(6)),
                    new ArtworkEntity(8L, "Urban Jungle", 125, 165, "2022", LocalDateTime.now(), "Gemalt mit Pinsel", new BigDecimal("140.00"), false, images.get(7)),
                    new ArtworkEntity(9L, "Golden Sunset", 135, 175, "2022", LocalDateTime.now(), "Öl auf Leinwand", new BigDecimal("180.00"), true, images.get(8)));
            artworkRepository.saveAll(artworks);
        };
    }

    public byte[] readFileToByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytesArray = new byte[(int) file.length()];
            fis.read(bytesArray);
            return bytesArray;
        }
    }

}
