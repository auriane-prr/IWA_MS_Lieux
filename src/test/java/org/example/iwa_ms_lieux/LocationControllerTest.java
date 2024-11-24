package org.example.iwa_ms_lieux;

import org.example.iwa_ms_lieux.controllers.LocationController;
import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.services.LocationService;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import org.example.iwa_ms_lieux.services.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private LocationService locationService;

    @MockBean
    private PhotoService photoService;

    private Location testLocation;

    @BeforeEach
    void setUp() {
        testLocation = new Location();
        testLocation.setLocationId(1);
        testLocation.setName("Test Location");
        testLocation.setVille("Test City");
    }

    @Test
    void testGetAllLocations() throws Exception {
        List<Location> locations = Arrays.asList(testLocation);
        Mockito.when(locationRepository.findAll()).thenReturn(locations);

        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].locationId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Location"))
                .andExpect(jsonPath("$[0].ville").value("Test City"));
    }

    @Test
    void testGetLocationById() throws Exception {
        Mockito.when(locationRepository.findById(1)).thenReturn(Optional.of(testLocation));

        mockMvc.perform(get("/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").value(1))
                .andExpect(jsonPath("$.name").value("Test Location"))
                .andExpect(jsonPath("$.ville").value("Test City"));
    }

    @Test
    void testGetLocationById_NotFound() throws Exception {
        Mockito.when(locationRepository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/locations/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateLocation() throws Exception {
        // Configure le comportement simulé du repository pour retourner un lieu avec une date de publication actuelle
        testLocation.setPublicationDate(LocalDate.now());
        Mockito.when(locationRepository.saveAndFlush(any(Location.class))).thenReturn(testLocation);

        String requestBody = """
                {
                    "name": "Test Location",
                    "ville": "Test City"
                }
                """;

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.locationId").value(1))
                .andExpect(jsonPath("$.name").value("Test Location"))
                .andExpect(jsonPath("$.ville").value("Test City"))
                .andExpect(jsonPath("$.publicationDate").value(LocalDate.now().toString())); // Vérifie que la date est la date actuelle
    }

    @Test
    void testDeleteLocation() throws Exception {
        // Mock pour vérifier l'existence du lieu
        Mockito.when(locationRepository.existsById(1)).thenReturn(true);

        // Mock pour simuler le comportement du service PhotoService
        Mockito.doNothing().when(photoService).deletePhotoByLocationId(1L);

        // Exécute la requête DELETE
        mockMvc.perform(delete("/locations/1"))
                .andExpect(status().isNoContent());

        // Vérifie que la méthode de suppression dans le PhotoService est appelée
        Mockito.verify(photoService, Mockito.times(1)).deletePhotoByLocationId(1L);

        // Vérifie que le lieu est supprimé
        Mockito.verify(locationRepository, Mockito.times(1)).deleteById(1);
    }


   @Test
    void testLinkEquipmentsToLocation() throws Exception {
        // Mock des données entrantes
        List<Integer> equipmentIds = List.of(101, 102, 103);
        String requestBody = new ObjectMapper().writeValueAsString(equipmentIds);

        // Mock du service
        Mockito.doNothing().when(locationService).linkEquipmentsToLocation(1, equipmentIds);

        // Exécution de la requête
        mockMvc.perform(post("/locations/1/equipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());

        // Vérification que le service est appelé avec les bons paramètres
        Mockito.verify(locationService, Mockito.times(1)).linkEquipmentsToLocation(1, equipmentIds);
    }

    @Test
    void testUnlinkEquipmentFromLocation() throws Exception {
        Mockito.doNothing().when(locationService).unlinkEquipmentFromLocation(1, 101);

        mockMvc.perform(delete("/locations/1/equipments/101"))
                .andExpect(status().isNoContent());
        Mockito.verify(locationService, Mockito.times(1)).unlinkEquipmentFromLocation(1, 101);
    }

@Test
    void testGetLocationsByUserId() throws Exception {
        testLocation.setUserId(101); // Assurez-vous que le userId est défini
        List<Location> locations = Arrays.asList(testLocation);

        Mockito.when(locationService.findLocationsByUserId(101)).thenReturn(locations);

        mockMvc.perform(get("/locations/user/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].locationId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Location"))
                .andExpect(jsonPath("$[0].userId").value(101));
    }

    @Test
    void testGetTopThreeRecentLocations() throws Exception {
        // Créez trois lieux avec des dates de publication différentes
        Location location1 = new Location();
        location1.setLocationId(1);
        location1.setName("Location 1");
        location1.setPublicationDate(LocalDate.now().minusDays(3)); // Il y a 3 jours

        Location location2 = new Location();
        location2.setLocationId(2);
        location2.setName("Location 2");
        location2.setPublicationDate(LocalDate.now().minusDays(2)); // Il y a 2 jours

        Location location3 = new Location();
        location3.setLocationId(3);
        location3.setName("Location 3");
        location3.setPublicationDate(LocalDate.now().minusDays(1)); // Hier

        // Simulez le service pour renvoyer les trois lieux
        List<Location> recentLocations = Arrays.asList(location3, location2, location1);
        Mockito.when(locationService.getThreeMostRecentLocations()).thenReturn(recentLocations);

        // Effectuez une requête GET sur la route des lieux récents
        mockMvc.perform(get("/locations/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3)) // Vérifiez qu'il y a 3 lieux
                .andExpect(jsonPath("$[0].locationId").value(3)) // Le plus récent en premier
                .andExpect(jsonPath("$[1].locationId").value(2))
                .andExpect(jsonPath("$[2].locationId").value(1));
    }

}
