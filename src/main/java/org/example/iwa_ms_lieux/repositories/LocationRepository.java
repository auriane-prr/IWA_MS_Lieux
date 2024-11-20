package org.example.iwa_ms_lieux.repositories;

import org.example.iwa_ms_lieux.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    // Rechercher un lieu par son nom
    Optional<Location> findByName(String name);

    // Rechercher des lieux par ville (insensible à la casse)
    List<Location> findByVilleIgnoreCase(String ville);

    // Rechercher des lieux par utilisateur (propriétaire)
    List<Location> findByUserId(Integer userId);

    // Classe les lieux par date de publication
    List<Location> findAllByOrderByPublicationDateDesc(Pageable pageable);
}
