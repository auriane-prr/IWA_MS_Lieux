package org.example.iwa_ms_lieux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.iwa_ms_lieux.models.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
}
