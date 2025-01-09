package com.vincent.gallerybackend.config;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.event.model.EventEntity;
import com.vincent.gallerybackend.exceptions.ImageFileException;
import com.vincent.gallerybackend.exhibition.model.ExhibitionEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileValidationService {

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    public void safeImageInEntity(ArtworkEntity entity, MultipartFile file) {
        saveImage(entity, file);
    }

    public void safeImageInEntity(ExhibitionEntity entity, MultipartFile file) {
        saveImage(entity, file);
    }

    public void safeImageInEntity(EventEntity entity, MultipartFile file) {
        saveImage(entity, file);
    }

    public void saveImage(Object entity, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new ImageFileException("Image must not be empty");
            }

            if (entity instanceof ArtworkEntity artwork) {
                artwork.setImage(file.getBytes());
            } else if (entity instanceof ExhibitionEntity exhibition) {
                exhibition.setImage(file.getBytes());
            } else if (entity instanceof EventEntity event) {
                event.setImage(file.getBytes());
            } else {
                throw new IllegalArgumentException("Unsupported entity type");
            }
        } catch (IOException e) {
            throw new ImageFileException("Could not save image");
        }
    }

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
    }

}
