package org.example.iwa_ms_lieux.controllers;

import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

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

    // Crée un nouveau lieu
    @PostMapping
    public ResponseEntity<Location> create(@RequestBody Location location) {
        Location createdLocation = locationRepository.saveAndFlush(location);
        return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
    }

    // Supprime un lieu par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!locationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with ID " + id + " not found");
        }
        locationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Met à jour un lieu existant
    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable Integer id, @RequestBody Location location) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with ID " + id + " not found"));

        // Copie des propriétés, ignore `locationId` pour ne pas écraser l'ID du lieu existant
        BeanUtils.copyProperties(location, existingLocation, "locationId");

        Location updatedLocation = locationRepository.saveAndFlush(existingLocation);
        return ResponseEntity.ok(updatedLocation);
    }
}
