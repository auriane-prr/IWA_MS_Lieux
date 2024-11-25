package org.example.iwa_ms_lieux;

import org.example.iwa_ms_lieux.models.Location;
import org.example.iwa_ms_lieux.services.LocationService;
import org.example.iwa_ms_lieux.controllers.SearchController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    private Location location1;
    private Location location2;

    @BeforeEach
    void setUp() {
        location1 = new Location();
        location1.setLocationId(1);
        location1.setName("Location 1");
        location1.setVille("Paris");

        location2 = new Location();
        location2.setLocationId(2);
        location2.setName("Location 2");
        location2.setVille("Paris");
    }

    @Test
    void testGetLocationsByEquipment_Success() throws Exception {
        // Mock response from LocationService
        when(locationService.getLocationsByEquipments(Arrays.asList(1, 2))).thenReturn(Arrays.asList(location1, location2));

        // Perform GET request and validate response
        mockMvc.perform(get("/search/1/locations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].locationId").value(1))
                .andExpect(jsonPath("$[0].name").value("Location 1"))
                .andExpect(jsonPath("$[1].locationId").value(2))
                .andExpect(jsonPath("$[1].name").value("Location 2"));
    }
    @Test
    void testSearchLocationsByVille_Success() throws Exception {
        // Mock response from LocationService
        when(locationService.findLocationsByVille("Paris")).thenReturn(Arrays.asList(location1, location2));

        // Perform GET request and validate response
        mockMvc.perform(get("/search/ville/Paris")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].locationId").value(1))
                .andExpect(jsonPath("$[0].ville").value("Paris"))
                .andExpect(jsonPath("$[1].locationId").value(2))
                .andExpect(jsonPath("$[1].ville").value("Paris"));
    }
    @Test
    void testGetLocationsByEquipment_EmptyList() throws Exception {
        // Mock empty response
        when(locationService.getLocationsByEquipments(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Perform GET request and validate response
        mockMvc.perform(get("/search/0/locations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSearchLocationsByVille_NoContent() throws Exception {
        // Mock empty response
        when(locationService.findLocationsByVille("UnknownCity")).thenReturn(Collections.emptyList());

        // Perform GET request and validate response
        mockMvc.perform(get("/search/ville/UnknownCity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
