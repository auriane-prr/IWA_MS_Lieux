package org.example.iwa_ms_lieux.controllers;

import org.example.iwa_ms_lieux.models.Equipment;
import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import org.example.iwa_ms_lieux.services.LocationService;
import org.example.iwa_ms_lieux.services.PhotoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PhotoService photoService;

    // Liste tous les lieux
    @GetMapping
    public ResponseEntity<List<Location>> list() {
        List<Location> locations = locationRepository.findAll();
        return ResponseEntity.ok(locations);
    }

    // Récupère un lieu par ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> get(@PathVariable Integer id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with ID " + id + " not found"));
        return ResponseEntity.ok(location);
    }

    // Récupérer un lieu par nom
    @GetMapping("/name/{name}")
    public ResponseEntity<Location> getByName(@PathVariable String name) {
        Location location = locationService.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with name " + name + " not found"));
        return ResponseEntity.ok(location);
    }

    // Crée un nouveau lieu
    @PostMapping
    public ResponseEntity<Location> create(@RequestBody Location location) {
        // Définir explicitement la date de publication pour éviter qu'elle soit remplacée
        location.setPublicationDate(LocalDate.now());
        Location createdLocation = locationRepository.saveAndFlush(location);
        return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
    }


    // Supprime un lieu par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!locationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with ID " + id + " not found");
        }

        // Supprime la photo associée au lieu
        try {
            photoService.deletePhotoByLocationId(Long.valueOf(id));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting photo: " + e.getMessage());
        }

        // Supprime le lieu
        locationRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }


    // Met à jour un lieu existant
    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable Integer id, @RequestBody Location location) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with ID " + id + " not found"));

        // Copie des propriétés de base sans écraser les relations existantes
        BeanUtils.copyProperties(location, existingLocation,
                "locationId", "userId", "publicationDate", "photos", "equipments");

        // Sauvegarde la mise à jour
        Location updatedLocation = locationRepository.saveAndFlush(existingLocation);
        return ResponseEntity.ok(updatedLocation);
    }


    // Lier un ou plusieurs équipement à un lieu
    @PostMapping("/{locationId}/equipments")
    public ResponseEntity<Void> linkEquipmentsToLocation(
            @PathVariable Integer locationId,
            @RequestBody List<Integer> equipmentIds) {
        locationService.linkEquipmentsToLocation(locationId, equipmentIds);
        return ResponseEntity.noContent().build();
    }

    // Délier un équipement d'un lieu
    @DeleteMapping("/{locationId}/equipments/{equipmentId}")
    public ResponseEntity<Void> unlinkEquipmentFromLocation(@PathVariable Integer locationId, @PathVariable Integer equipmentId) {
        locationService.unlinkEquipmentFromLocation(locationId, equipmentId);
        return ResponseEntity.noContent().build();
    }

    // Récupérer les lieux par propriétaire
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Location>> getLocationsByUserId(@PathVariable Integer userId) {
        List<Location> locations = locationService.findLocationsByUserId(userId);
        // Retourne une liste vide au lieu de NO_CONTENT
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Location>> getThreeMostRecentLocations() {
        List<Location> locations = locationService.getThreeMostRecentLocations();
        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(locations);
    }


}
