package org.example.iwa_ms_lieux.controllers;

import org.example.iwa_ms_lieux.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/{locationId}")
    public ResponseEntity<String> addPhotoAndLinkToLocation(
            @PathVariable Long locationId,
            @RequestParam MultipartFile photo) {
        try {
            photoService.addPhotoAndLinkToLocation(locationId, photo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Photo added and linked to location successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving photo: " + e.getMessage());
        }
    }

    @GetMapping("/get/{photoId}")
    public ResponseEntity<Resource> getPhotoById(@PathVariable Long photoId) {
        try {
            Optional<Path> photoPath = photoService.getPhotoPathById(photoId);
            if (photoPath.isPresent()) {
                Resource fileResource = new UrlResource(photoPath.get().toUri());
                return ResponseEntity.ok()
                        .header("Content-Type", "image/jpeg") // Ajuste le type MIME selon ton besoin
                        .body(fileResource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
