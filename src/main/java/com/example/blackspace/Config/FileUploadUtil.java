package com.example.blackspace.Config;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

public final class FileUploadUtil {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private static final Set<String> ALLOWED_AUDIO_TYPES = Set.of(
            "audio/mpeg", "audio/wav", "audio/ogg", "audio/mp4", "audio/webm"
    );

    private FileUploadUtil() {}

    public static void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return;
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid image type: " + contentType
                    + ". Allowed: " + ALLOWED_IMAGE_TYPES);
        }
    }

    public static void validateAudio(MultipartFile file) {
        if (file == null || file.isEmpty()) return;
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_AUDIO_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid audio type: " + contentType
                    + ". Allowed: " + ALLOWED_AUDIO_TYPES);
        }
    }

    public static String sanitizeFilename(String originalFilename, String extension) {
        return UUID.randomUUID() + extension;
    }
}
