package org.example.iwa_ms_lieux.controllers;

import org.example.iwa_ms_lieux.models.LocationPhoto;
import org.example.iwa_ms_lieux.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations/{locationId}/photos")
public class LocationPhotoController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<LocationPhoto> getPhotosByLocation(@PathVariable Integer locationId) {
        return locationService.getPhotosByLocation(locationId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationPhoto addPhotoToLocation(@PathVariable Integer locationId, @RequestBody LocationPhoto photo) {
        return locationService.addPhotoToLocation(locationId, photo);
    }

    @DeleteMapping("/{photoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhoto(@PathVariable Integer locationId, @PathVariable Integer photoId) {
        locationService.deletePhotoFromLocation(locationId, photoId);
    }
}
