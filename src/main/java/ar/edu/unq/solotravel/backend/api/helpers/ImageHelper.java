package ar.edu.unq.solotravel.backend.api.helpers;

import ar.edu.unq.solotravel.backend.api.exceptions.InvalidImageException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;

@Component
public class ImageHelper {
    private static final String[] allowedExtensions = new String[] {"webp", "jpg", "jpeg", "png"};

    public void ValidateImage(MultipartFile image) {
        try {
            String[] splittedFileName = Objects.requireNonNull(image.getOriginalFilename()).split("\\.");
            String extension = splittedFileName[splittedFileName.length - 1];
            if (image.isEmpty() || !isAllowedExtension(extension))
                throw new InvalidImageException();
        }
        catch (Exception ex) {
            throw new InvalidImageException();
        }
    }

    private boolean isAllowedExtension(String extension) {
        return Arrays.asList(allowedExtensions).contains(extension.toLowerCase());
    }
}
