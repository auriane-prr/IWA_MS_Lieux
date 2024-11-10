package org.example.iwa_ms_lieux.repositories;

import org.example.iwa_ms_lieux.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    // Rechercher un lieu par son nom
    Optional<Location> findByName(String name);
}
