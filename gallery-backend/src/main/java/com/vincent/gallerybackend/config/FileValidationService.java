package com.vincent.gallerybackend.config;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.exceptions.ImageFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileValidationService {

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
    }

    public void safeImageInEntity(ArtworkEntity artworkEntity, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new ImageFileException("Image must be not empty");
            }
            artworkEntity.setImage(file.getBytes());
        } catch (IOException e) {
            throw new ImageFileException("Could not safe image");
        }
    }

}
