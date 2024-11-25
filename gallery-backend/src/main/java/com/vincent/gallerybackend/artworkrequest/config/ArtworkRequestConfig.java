//package com.vincent.gallerybackend.artworkrequest.config;
//
//import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestEntity;
//import com.vincent.gallerybackend.artworkrequest.repository.ArtworkRequestRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@AllArgsConstructor
//@Profile("!test")
//@Configuration
//public class ArtworkRequestConfig {
//
//    private final ResourceLoader resourceLoader;
//
//    @Bean
//    CommandLineRunner artworkCommandLineRunner(ArtworkRequestRepository artworkRequestRepository) throws IOException {
//        List<byte[]> images = new ArrayList<>();
//        for (int i = 1; i < 10; i++) {
//            String path = "classpath:" + i + ".jpg";
//            byte[] base64 = readFileToByteArray(path);
//            images.add(base64);
//        }
//
//        return args -> {
//            List<ArtworkRequestEntity> artworks = List.of(
//                new ArtworkRequestEntity(1L, "Ist das Kunstwerk noch zu verkaufen?", LocalDateTime.now())
//                    );
//            artworkRequestRepository.saveAll(artworks);
//        };
//    }
//
//    public byte[] readFileToByteArray(String filePath) throws IOException {
//        Resource resource = resourceLoader.getResource(filePath);
//        try (FileInputStream fis = new FileInputStream(resource.getFile())) {
//            byte[] bytesArray = new byte[(int) resource.contentLength()];
//            fis.read(bytesArray);
//            return bytesArray;
//        }
//    }
//
//}
