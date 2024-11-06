package org.example.iwa_ms_lieux.services;

import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.models.LocationPhoto;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import org.example.iwa_ms_lieux.repositories.LocationPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationPhotoRepository locationPhotoRepository;

    public List<LocationPhoto> getPhotosByLocation(Integer locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return locationPhotoRepository.findByLocation_LocationId(locationId);
    }

    public LocationPhoto addPhotoToLocation(Integer locationId, LocationPhoto photo) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        photo.setLocation(location);
        return locationPhotoRepository.save(photo);
    }

    public void deletePhotoFromLocation(Integer locationId, Integer photoId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        LocationPhoto photo = locationPhotoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        if (!photo.getLocation().getLocationId().equals(locationId)) {
            throw new RuntimeException("Photo does not belong to this location");
        }

        locationPhotoRepository.delete(photo);
    }

    // Autres méthodes pour la gestion des lieux
}
