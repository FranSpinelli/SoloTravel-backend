package ar.edu.unq.solotravel.backend.api.controllers;

import ar.edu.unq.solotravel.backend.api.helpers.ImageHelper;
import ar.edu.unq.solotravel.backend.api.services.GoogleCloudStorageService;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@CrossOrigin()
@RestController
@RequestMapping("/images")
public class ImagesController {

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;
    @Autowired
    private ImageHelper imageHelper;

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity uploadImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("image") MultipartFile image) throws IOException {

        imageHelper.ValidateImage(image);
        Blob result = googleCloudStorageService.uploadFile(image);

        return ResponseEntity.ok().body(result.getName());
    }
}
