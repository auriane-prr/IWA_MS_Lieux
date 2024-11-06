package org.example.iwa_ms_lieux.repositories;

import org.example.iwa_ms_lieux.models.LocationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationPhotoRepository extends JpaRepository<LocationPhoto, Integer> {
    List<LocationPhoto> findByLocation_LocationId(Integer locationId);
}

