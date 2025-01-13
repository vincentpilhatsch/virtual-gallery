package com.vincent.gallerybackend.artworkrequest.config;

import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestEntity;
import com.vincent.gallerybackend.artworkrequest.repository.ArtworkRequestRepository;
import lombok.AllArgsConstructor;
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
import java.util.UUID;

@AllArgsConstructor
@Profile("!test")
@Configuration
public class ArtworkRequestConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    CommandLineRunner artworkRequestCommandLineRunner(ArtworkRequestRepository artworkRequestRepository) throws IOException {
        List<byte[]> images = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            String path = "classpath:" + i + ".jpg";
            byte[] base64 = readFileToByteArray(path);
            images.add(base64);
        }

        return args -> {
            List<ArtworkRequestEntity> artworkRequestEntities = List.of(
//                    new ArtworkRequestEntity(1L, "Ich finde Ihre Kunst sehr interessant und habe Interesse an dem Kunstwerk.", LocalDateTime.now(), UUID.fromString("0680e076-77cf-4f65-a8f9-cc04024da51d"), 1L),
//                    new ArtworkRequestEntity(2L, "Ihre Kunst ist sehr beeindruckend, bitte treten Sie sich mit mir in Kontakt.", LocalDateTime.now(), UUID.fromString("62b5079f-43cd-4a4d-8365-b383b9ee4010"), 2L),
//                    new ArtworkRequestEntity(3L, "Mir gefällt dieses Kunstwerk. Ist es noch zu erwerben?", LocalDateTime.now(), UUID.fromString("0680e076-77cf-4f65-a8f9-cc04024da51d"), 3L)
            );
            artworkRequestRepository.saveAll(artworkRequestEntities);
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
