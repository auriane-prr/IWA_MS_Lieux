package org.example.iwa_ms_lieux.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name="locations")
@Access(AccessType.FIELD)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    private Integer userId;
    private String name;

    @Column(precision = 18, scale = 16)
    private BigDecimal latitude;

    @Column(precision = 18, scale = 16)
    private BigDecimal longitude;

    private String adresse;
    private String ville;
    private String codePostal;
    private String description;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LocationPhoto> photos;

    public Location() {}

    public Location(Integer userId, String name, BigDecimal latitude, BigDecimal longitude, String adresse, String ville, String codePostal, String description) {
        this.userId = userId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.description = description;
    }

    // Getters and setters
    public Integer getLocationId() { return locationId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}


