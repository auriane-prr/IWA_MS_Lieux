package org.example.iwa_ms_lieux;

import org.example.iwa_ms_lieux.controllers.LocationController;
import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.services.LocationService;
import org.example.iwa_ms_lieux.repositories.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
                .andExpect(jsonPath("$.ville").value("Test City"));
    }

    @Test
    void testDeleteLocation() throws Exception {
        Mockito.when(locationRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/locations/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(locationRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void testDeleteLocation_NotFound() throws Exception {
        Mockito.when(locationRepository.existsById(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/locations/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testLinkEquipmentToLocation() throws Exception {
        Mockito.doNothing().when(locationService).linkEquipmentToLocation(1, 101);

        mockMvc.perform(post("/locations/1/equipments/101"))
                .andExpect(status().isNoContent());
        Mockito.verify(locationService, Mockito.times(1)).linkEquipmentToLocation(1, 101);
    }

    @Test
    void testUnlinkEquipmentFromLocation() throws Exception {
        Mockito.doNothing().when(locationService).unlinkEquipmentFromLocation(1, 101);

        mockMvc.perform(delete("/locations/1/equipments/101"))
                .andExpect(status().isNoContent());
        Mockito.verify(locationService, Mockito.times(1)).unlinkEquipmentFromLocation(1, 101);
    }
}
