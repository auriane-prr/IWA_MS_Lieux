package org.example.iwa_ms_lieux.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "equipments")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer equipmentId;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToMany(mappedBy = "equipments")
    @JsonIgnore  // Empêche la sérialisation de la relation locations pour éviter les boucles infinies
    private Set<Location> locations;

    // Getters et setters
    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
}
