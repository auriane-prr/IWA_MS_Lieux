package org.example.iwa_ms_lieux.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.models.LocationPhoto;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import org.example.iwa_ms_lieux.repositories.LocationPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.example.iwa_ms_lieux.repositories.EquipmentRepository;
import org.example.iwa_ms_lieux.models.Equipment;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationPhotoRepository locationPhotoRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    // Ajouter un nouveau lieu
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }


    // Lier un équipement à un lieu
    public void linkEquipmentToLocation(Integer locationId, Integer equipmentId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        location.getEquipments().add(equipment);
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
    public List<Location> getLocationsByEquipment(Integer equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        return List.copyOf(equipment.getLocations());
    }

    // Récupérer un lieu par son nom
    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }

    // Récupérer toutes les photos d'un lieu
    public List<LocationPhoto> getPhotosByLocation(Integer locationId) {
        locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        return locationPhotoRepository.findByLocation_LocationId(locationId);
    }

    // Ajouter une photo à un lieu
    public LocationPhoto addPhotoToLocation(Integer locationId, LocationPhoto photo) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        photo.setLocation(location);
        return locationPhotoRepository.save(photo);
    }

    // Supprimer une photo d'un lieu
    public void deletePhotoFromLocation(Integer locationId, Integer photoId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        LocationPhoto photo = locationPhotoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));

        if (!photo.getLocation().getLocationId().equals(locationId)) {
            throw new RuntimeException("Photo does not belong to this location");
        }

        locationPhotoRepository.delete(photo);
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
