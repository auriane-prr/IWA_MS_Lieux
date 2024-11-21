package org.example.iwa_ms_lieux.services;

import org.example.iwa_ms_lieux.models.Photo;
import org.example.iwa_ms_lieux.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoService {

    private static final String UPLOAD_DIR = "/app/images"; // Répertoire local

    @Autowired
    private PhotoRepository photoRepository;

    public void addPhotoAndLinkToLocation(Long locationId, MultipartFile file) throws IOException {
        // Générer un nom unique pour le fichier
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Construire le chemin complet
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Créer les répertoires si nécessaire
        Files.createDirectories(filePath.getParent());

        // Sauvegarder le fichier sur le disque
        Files.write(filePath, file.getBytes());

        // Sauvegarder le chemin dans la base de données
        Photo photo = new Photo(filePath.toString());
        Photo savedPhoto = photoRepository.save(photo);

        // Ajouter la relation dans la table de jointure
        photoRepository.linkPhotoToLocation(locationId, savedPhoto.getPhotoId());
    }

    public Optional<Path> getPhotoPathById(Long photoId) {
        return photoRepository.findById(photoId)
                .map(photo -> Paths.get(photo.getPhotoPath()));
    }

    public void deletePhotoByLocationId(Long locationId) throws IOException {
        // Trouver l'ID de la photo associée au lieu
        Long photoId = photoRepository.findPhotoIdByLocationId(locationId);

        if (photoId != null) {
            // Supprimer le fichier local
            Optional<Photo> photo = photoRepository.findById(photoId);
            if (photo.isPresent()) {
                Files.deleteIfExists(Paths.get(photo.get().getPhotoPath()));
            }

            // Supprimer l'entrée dans la table `photos`
            photoRepository.deleteById(photoId);
        }
    }
}
