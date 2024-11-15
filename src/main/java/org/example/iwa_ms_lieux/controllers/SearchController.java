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
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private LocationService locationService;

    // Récupérer les lieux pour un équipement donné
    @GetMapping("{equipmentId}/locations")
    public ResponseEntity<List<Location>> getLocationsByEquipment(@PathVariable Integer equipmentId) {
        return ResponseEntity.ok(locationService.getLocationsByEquipment(equipmentId));
    }

    @GetMapping("/locations/ville/{ville}")
    public ResponseEntity<List<Location>> searchLocationsByVille(@PathVariable String ville) {
        List<Location> locations = locationService.findLocationsByVille(ville);
        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucun lieu trouvé
        }
        return ResponseEntity.ok(locations); // 200 OK avec les lieux trouvés
    }



}