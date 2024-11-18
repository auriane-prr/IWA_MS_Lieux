package org.example.iwa_ms_lieux;

import org.example.iwa_ms_lieux.controllers.EquipmentController;
import org.example.iwa_ms_lieux.models.Equipment;
import org.example.iwa_ms_lieux.services.EquipmentService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipmentController.class)
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    private Equipment testEquipment;

    @BeforeEach
    void setUp() {
        testEquipment = new Equipment();
        testEquipment.setEquipmentId(1);
        testEquipment.setName("Test Equipment");
    }

    @Test
    void testAddEquipment() throws Exception {
        when(equipmentService.addEquipment(any(Equipment.class))).thenReturn(testEquipment);

        String requestBody = """
            {
                "equipmentId": 1,
                "name": "Test Equipment"
            }
            """;

        mockMvc.perform(post("/equipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipmentId").value(1))
                .andExpect(jsonPath("$.name").value("Test Equipment"));
    }

    @Test
    void testDeleteEquipment() throws Exception {
        doNothing().when(equipmentService).deleteEquipment(1);

        mockMvc.perform(delete("/equipments/1"))
                .andExpect(status().isNoContent());
        verify(equipmentService, times(1)).deleteEquipment(1);
    }

    @Test
    void testGetAllEquipments() throws Exception {
        when(equipmentService.getAllEquipments()).thenReturn(Arrays.asList(testEquipment));

        mockMvc.perform(get("/equipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].equipmentId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Equipment"));
    }

    @Test
    void testGetEquipmentsByLocation() throws Exception {
        when(equipmentService.getEquipmentsByLocation(101)).thenReturn(Arrays.asList(testEquipment));

        mockMvc.perform(get("/equipments/101/equipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].equipmentId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Equipment"));
    }
}
