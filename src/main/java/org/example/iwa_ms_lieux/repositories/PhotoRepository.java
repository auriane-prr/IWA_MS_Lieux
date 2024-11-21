package org.example.iwa_ms_lieux.repositories;

import org.example.iwa_ms_lieux.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO location_photos (location_id, photo_id) VALUES (?1, ?2)", nativeQuery = true)
    void linkPhotoToLocation(Long locationId, Long photoId);

    @Query(value = "SELECT photo_id FROM location_photos WHERE location_id = ?1", nativeQuery = true)
    Long findPhotoIdByLocationId(Long locationId);
}
