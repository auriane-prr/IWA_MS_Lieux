package org.example.iwa_ms_lieux.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.iwa_ms_lieux.repositories.EquipmentRepository;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import org.example.iwa_ms_lieux.models.Equipment;
import org.example.iwa_ms_lieux.models.Location;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private LocationRepository locationRepository;


    // Ajouter un nouvel équipement
    public Equipment addEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    // Supprimer un équipement
    public void deleteEquipment(Integer equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        equipmentRepository.delete(equipment);
    }


    // Récupérer tous les équipements
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    // Récupérer les équipements pour un lieu donné
    public List<Equipment> getEquipmentsByLocation(Integer locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        return List.copyOf(location.getEquipments());
    }
}
