package com.vincent.gallerybackend.notification.config;

import com.vincent.gallerybackend.notification.model.NotificationEntity;
import com.vincent.gallerybackend.notification.repository.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;

@Profile("!test")
@Configuration
public class NotificationConfig {

    @Bean
    CommandLineRunner notificationCommandLineRunner(NotificationRepository notificationRepository) {
        return args -> {
            NotificationEntity notificationEntity1 = new NotificationEntity(
                    1L,
                    "Neues Kunstwerk!",
                    "Lebende Stille wurde hochgeladen",
                    LocalDateTime.now(),
                    false
            );
            NotificationEntity notificationEntity2 = new NotificationEntity(
                    2L,
                    "Neues Event!",
                    "Treffen in der Galerie",
                    LocalDateTime.now(),
                    true
            );
            notificationRepository.saveAll(List.of(notificationEntity1, notificationEntity2));
        };
    }

}
