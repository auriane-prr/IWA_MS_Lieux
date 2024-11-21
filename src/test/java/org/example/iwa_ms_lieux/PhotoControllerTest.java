package org.example.iwa_ms_lieux;

import org.example.iwa_ms_lieux.controllers.PhotoController;
import org.example.iwa_ms_lieux.services.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhotoController.class)
public class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    private Path testPhotoPath;

    @BeforeEach
    void setUp() {
        testPhotoPath = Paths.get("/app/images/test-photo.jpg");
    }

    @Test
    void testAddPhotoAndLinkToLocation() throws Exception {
        // Mock du service pour ne pas lever d'exception
        Mockito.doNothing().when(photoService).addPhotoAndLinkToLocation(anyLong(), any(MultipartFile.class));

        // Simule l'envoi d'une requête POST avec un fichier
        mockMvc.perform(multipart("/photos/1")
                        .file("photo", "test-image-content".getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().string("Photo added and linked to location successfully"));

        // Vérifie que la méthode du service est bien appelée
        Mockito.verify(photoService, Mockito.times(1)).addPhotoAndLinkToLocation(eq(1L), any(MultipartFile.class));
    }

    @Test
    void testAddPhotoAndLinkToLocation_Error() throws Exception {
        // Simule une exception dans le service
        Mockito.doThrow(new IOException("Simulated error")).when(photoService).addPhotoAndLinkToLocation(anyLong(), any(MultipartFile.class));

        // Simule l'envoi d'une requête POST avec un fichier
        mockMvc.perform(multipart("/photos/1")
                        .file("photo", "test-image-content".getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error saving photo: Simulated error"));

        // Vérifie que la méthode du service est bien appelée
        Mockito.verify(photoService, Mockito.times(1)).addPhotoAndLinkToLocation(eq(1L), any(MultipartFile.class));
    }
}

