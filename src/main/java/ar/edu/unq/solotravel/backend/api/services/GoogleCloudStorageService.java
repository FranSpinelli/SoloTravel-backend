package ar.edu.unq.solotravel.backend.api.services;

import ar.edu.unq.solotravel.backend.api.exceptions.InvalidImageException;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GoogleCloudStorageService {
    @Value("${google.cloud.storage.bucket-name}")
    private String bucketName;

    public Blob uploadFile(MultipartFile file) {
        Blob uploadedBlob = null;
        try {
            String blobName = file.getOriginalFilename();
            Storage storage = StorageOptions.getDefaultInstance().getService();
            BlobId blobId = BlobId.of(bucketName, blobName == null ? "image" : blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

            uploadedBlob = storage.create(blobInfo, file.getBytes());
        }
        catch (Exception ex) {
            throw new InvalidImageException();
        }
        return uploadedBlob;
    }
}
