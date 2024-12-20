package org.example.iwa_ms_lieux.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import java.util.HashSet; // Since you're also using HashSet
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.example.iwa_ms_lieux.repositories.EquipmentRepository;
import org.example.iwa_ms_lieux.models.Equipment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    // Ajouter un nouveau lieu
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }


    // Lier plusieurs équipements à un lieu
    public void linkEquipmentsToLocation(Integer locationId, List<Integer> equipmentIds) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        List<Equipment> equipments = equipmentRepository.findAllById(equipmentIds);
        if (equipments.size() != equipmentIds.size()) {
            throw new EntityNotFoundException("One or more equipments not found");
        }

        location.getEquipments().addAll(equipments);
        locationRepository.save(location);
    }

    // Délier un équipement d'un lieu
    public void unlinkEquipmentFromLocation(Integer locationId, Integer equipmentId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        location.getEquipments().remove(equipment);
        locationRepository.save(location);
    }


    // Récupérer les lieux pour un équipement donné
public List<Location> getLocationsByEquipments(List<Integer> equipmentIds) {
    List<Equipment> equipments = equipmentRepository.findAllById(equipmentIds);
    Set<Location> uniqueLocations = new HashSet<>(); 
    equipments.forEach(equipment -> uniqueLocations.addAll(equipment.getLocations()));
    return new ArrayList<>(uniqueLocations);
}


    // Récupérer un lieu par son nom
    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }


    // Rechercher les lieux par ville
    public List<Location> findLocationsByVille(String ville) {
        return locationRepository.findByVilleIgnoreCase(ville);
    }

    // Récupérer les lieux par propriétaire (userId)
    public List<Location> findLocationsByUserId(Integer userId) {
        return locationRepository.findByUserId(userId);
    }

    public List<Location> getThreeMostRecentLocations() {
        Pageable topThree = PageRequest.of(0, 3); // Limiter à 3 résultats
        return locationRepository.findAllByOrderByPublicationDateDesc(topThree);
    }


}
