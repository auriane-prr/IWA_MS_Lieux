package org.example.iwa_ms_lieux.controllers;

import org.example.iwa_ms_lieux.models.Equipment;
import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import org.example.iwa_ms_lieux.services.LocationService;
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

    @Autowired
    private LocationService locationService;

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

        // Copie des propriétés de base sans écraser les relations existantes
        BeanUtils.copyProperties(location, existingLocation, "locationId", "photos", "equipments");

        // Sauvegarde la mise à jour
        Location updatedLocation = locationRepository.saveAndFlush(existingLocation);
        return ResponseEntity.ok(updatedLocation);
    }


    // Lier un équipement à un lieu
    @PostMapping("/{locationId}/equipments/{equipmentId}")
    public ResponseEntity<Void> linkEquipmentToLocation(@PathVariable Integer locationId, @PathVariable Integer equipmentId) {
        locationService.linkEquipmentToLocation(locationId, equipmentId);
        return ResponseEntity.noContent().build();
    }

    // Délier un équipement d'un lieu
    @DeleteMapping("/{locationId}/equipments/{equipmentId}")
    public ResponseEntity<Void> unlinkEquipmentFromLocation(@PathVariable Integer locationId, @PathVariable Integer equipmentId) {
        locationService.unlinkEquipmentFromLocation(locationId, equipmentId);
        return ResponseEntity.noContent().build();
    }


}
