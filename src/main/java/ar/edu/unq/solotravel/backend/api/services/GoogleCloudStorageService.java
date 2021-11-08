package ar.edu.unq.solotravel.backend.api.services;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        catch (IOException exception) {
            //TODO: Launch custom exception and add it to global handler exception
        }
        return uploadedBlob;
    }
}
