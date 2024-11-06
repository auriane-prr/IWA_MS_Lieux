package org.example.iwa_ms_lieux.repositories;

import org.example.iwa_ms_lieux.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> { }
