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
                    "Geschlossene Gesellschaft",
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et\n" + "          dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam.",
                    "Hohenzollerndamm 216, 10781 Berlin",
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(2),
                    images.get(1)
            );
            ExhibitionEntity exhibitionEntity2 = new ExhibitionEntity(
                    2L,
                    "Never Love",
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo irmod tempor.",
                    "Berliner Straße 16, 11721 Berlin",
                    LocalDate.now().minusDays(2),
                    LocalDate.now().minusDays(1),
                    images.get(3)
            );
            ExhibitionEntity exhibitionEntity3 = new ExhibitionEntity(
                    3L,
                    "The Wet Fever",
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo irmod tempor.",
                    "Eisenacher Straße 16, 11721 Berlin",
                    LocalDate.now().minusDays(2),
                    LocalDate.now().minusDays(1),
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
