package org.example.iwa_ms_lieux.controllers;

import org.example.iwa_ms_lieux.models.Equipment;
import org.example.iwa_ms_lieux.services.EquipmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    // Ajouter un nouvel équipement
    @PostMapping()
    public ResponseEntity<Equipment> addEquipment(@RequestBody Equipment equipment) {
        return ResponseEntity.ok(equipmentService.addEquipment(equipment));
    }

    // Supprimer un équipement par ID
    @DeleteMapping("{equipmentId}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Integer equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return ResponseEntity.noContent().build();
    }

    // Récupérer tous les équipements
    @GetMapping()
    public ResponseEntity<List<Equipment>> getAllEquipments() {
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    // Récupérer les équipements pour un lieu donné
    @GetMapping("/{locationId}/equipments")
    public ResponseEntity<List<Equipment>> getEquipmentsByLocation(@PathVariable Integer locationId) {
        return ResponseEntity.ok(equipmentService.getEquipmentsByLocation(locationId));
    }


}